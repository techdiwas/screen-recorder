package com.techdiwas.screenrecorder.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.techdiwas.screenrecorder.data.AudioSource
import com.techdiwas.screenrecorder.data.FileManager
import com.techdiwas.screenrecorder.data.RecordingConfig
import com.techdiwas.screenrecorder.data.RecordingState
import com.techdiwas.screenrecorder.data.VideoQuality
import com.techdiwas.screenrecorder.domain.RecordingController
import com.techdiwas.screenrecorder.service.RecordingService
import com.techdiwas.screenrecorder.ui.theme.ScreenRecorderTheme
import com.techdiwas.screenrecorder.util.Constants
import com.techdiwas.screenrecorder.util.PermissionHelper

/**
 * Main activity that displays the recording configuration UI.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            ScreenRecorderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == Constants.OVERLAY_PERMISSION_REQUEST_CODE) {
            if (PermissionHelper.hasOverlayPermission(this)) {
                Toast.makeText(this, "Overlay permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Overlay permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    val fileManager = remember { FileManager(context) }
    val recordingController = remember { RecordingController(context) }
    
    // Permission launchers
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (!allGranted) {
            Toast.makeText(context, "Some permissions were denied", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Media projection permission launcher
    val mediaProjectionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            // Start countdown then recording
            viewModel.startCountdown {
                viewModel.updateRecordingState(RecordingState.RECORDING)
                
                val outputFile = fileManager.createOutputFile()
                if (outputFile != null) {
                    val config = RecordingConfig(
                        audioSource = uiState.audioSource,
                        videoQuality = uiState.videoQuality,
                        outputFile = outputFile
                    )
                    recordingController.startRecording(result.resultCode, result.data!!, config)
                    
                    // Show overlay permission prompt if needed
                    if (!PermissionHelper.hasOverlayPermission(context)) {
                        PermissionHelper.requestOverlayPermission(
                            activity,
                            Constants.OVERLAY_PERMISSION_REQUEST_CODE
                        )
                    }
                } else {
                    Toast.makeText(context, "Failed to create output file", Toast.LENGTH_SHORT).show()
                    viewModel.resetToIdle()
                }
            }
        } else {
            Toast.makeText(context, "Screen capture permission denied", Toast.LENGTH_SHORT).show()
            viewModel.resetToIdle()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Screen Recorder") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Countdown or recording status
            if (uiState.recordingState == RecordingState.COUNTDOWN) {
                Text(
                    text = context.getString(
                        com.techdiwas.screenrecorder.R.string.countdown_message,
                        uiState.countdownValue
                    ),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (uiState.recordingState == RecordingState.RECORDING) {
                Text(
                    text = context.getString(com.techdiwas.screenrecorder.R.string.recording_in_progress),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            // Audio Source Selection
            if (uiState.recordingState == RecordingState.IDLE) {
                AudioSourceSelector(
                    selectedSource = uiState.audioSource,
                    onSourceSelected = { viewModel.updateAudioSource(it) }
                )
                
                // Video Quality Selection
                VideoQualitySelector(
                    selectedQuality = uiState.videoQuality,
                    onQualitySelected = { viewModel.updateVideoQuality(it) }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Start/Stop Recording Button
            Button(
                onClick = {
                    when (uiState.recordingState) {
                        RecordingState.IDLE -> {
                            // Check permissions first
                            val requiredPermissions = PermissionHelper.getRequiredPermissions()
                            val permissionsToRequest = requiredPermissions.filter {
                                !PermissionHelper.hasAudioPermission(context) ||
                                        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                                !PermissionHelper.hasNotificationPermission(context))
                            }
                            
                            if (permissionsToRequest.isNotEmpty()) {
                                permissionLauncher.launch(requiredPermissions)
                            } else {
                                // Request media projection
                                mediaProjectionLauncher.launch(recordingController.getMediaProjectionIntent())
                            }
                        }
                        RecordingState.RECORDING, RecordingState.PAUSED -> {
                            recordingController.stopRecording()
                            viewModel.resetToIdle()
                        }
                        else -> { /* Ignore other states */ }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = uiState.recordingState != RecordingState.COUNTDOWN,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.recordingState == RecordingState.IDLE)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = if (uiState.recordingState == RecordingState.IDLE)
                        context.getString(com.techdiwas.screenrecorder.R.string.start_recording)
                    else
                        context.getString(com.techdiwas.screenrecorder.R.string.stop_recording),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSourceSelector(
    selectedSource: AudioSource,
    onSourceSelected: (AudioSource) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Text(
            text = context.getString(com.techdiwas.screenrecorder.R.string.audio_source),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = getAudioSourceLabel(selectedSource, context),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                AudioSource.entries.forEach { source ->
                    DropdownMenuItem(
                        text = { Text(getAudioSourceLabel(source, context)) },
                        onClick = {
                            onSourceSelected(source)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoQualitySelector(
    selectedQuality: VideoQuality,
    onQualitySelected: (VideoQuality) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Text(
            text = context.getString(com.techdiwas.screenrecorder.R.string.video_quality),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedQuality.displayName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                VideoQuality.entries.forEach { quality ->
                    DropdownMenuItem(
                        text = { Text(quality.displayName) },
                        onClick = {
                            onQualitySelected(quality)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

private fun getAudioSourceLabel(source: AudioSource, context: android.content.Context): String {
    return when (source) {
        AudioSource.NONE -> context.getString(com.techdiwas.screenrecorder.R.string.audio_none)
        AudioSource.MICROPHONE -> context.getString(com.techdiwas.screenrecorder.R.string.audio_microphone)
        AudioSource.DEVICE -> context.getString(com.techdiwas.screenrecorder.R.string.audio_device)
        AudioSource.BOTH -> context.getString(com.techdiwas.screenrecorder.R.string.audio_both)
    }
}
