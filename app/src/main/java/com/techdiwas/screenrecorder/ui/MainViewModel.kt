package com.techdiwas.screenrecorder.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.techdiwas.screenrecorder.data.AudioSource
import com.techdiwas.screenrecorder.data.RecordingState
import com.techdiwas.screenrecorder.data.VideoQuality
import com.techdiwas.screenrecorder.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the main screen.
 * Manages recording state and configuration.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RecordingUiState())
    val uiState: StateFlow<RecordingUiState> = _uiState.asStateFlow()

    /**
     * Updates the selected audio source.
     */
    fun updateAudioSource(audioSource: AudioSource) {
        _uiState.value = _uiState.value.copy(audioSource = audioSource)
    }

    /**
     * Updates the selected video quality.
     */
    fun updateVideoQuality(videoQuality: VideoQuality) {
        _uiState.value = _uiState.value.copy(videoQuality = videoQuality)
    }

    /**
     * Starts the countdown before recording.
     */
    fun startCountdown(onComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                recordingState = RecordingState.COUNTDOWN,
                countdownValue = Constants.COUNTDOWN_SECONDS
            )

            for (i in Constants.COUNTDOWN_SECONDS downTo 1) {
                _uiState.value = _uiState.value.copy(countdownValue = i)
                delay(Constants.COUNTDOWN_INTERVAL_MS)
            }

            onComplete()
        }
    }

    /**
     * Updates the recording state.
     */
    fun updateRecordingState(state: RecordingState) {
        _uiState.value = _uiState.value.copy(recordingState = state)
    }

    /**
     * Resets to idle state.
     */
    fun resetToIdle() {
        _uiState.value = _uiState.value.copy(
            recordingState = RecordingState.IDLE,
            countdownValue = 0
        )
    }
}

/**
 * UI state for the recording screen.
 */
data class RecordingUiState(
    val recordingState: RecordingState = RecordingState.IDLE,
    val audioSource: AudioSource = AudioSource.NONE,
    val videoQuality: VideoQuality = VideoQuality.QUALITY_720P,
    val countdownValue: Int = 0
)
