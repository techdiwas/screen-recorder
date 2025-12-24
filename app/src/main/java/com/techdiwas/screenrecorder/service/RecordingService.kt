package com.techdiwas.screenrecorder.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.techdiwas.screenrecorder.R
import com.techdiwas.screenrecorder.data.AudioSource
import com.techdiwas.screenrecorder.data.FileManager
import com.techdiwas.screenrecorder.data.VideoQuality
import com.techdiwas.screenrecorder.ui.MainActivity
import com.techdiwas.screenrecorder.util.Constants
import java.io.File
import java.io.IOException

/**
 * Foreground service that handles screen recording.
 * Uses MediaProjection API and MediaRecorder.
 */
class RecordingService : Service() {

    companion object {
        private const val TAG = "RecordingService"
        private var isRecording = false

        fun isRecording(): Boolean = isRecording
    }

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null
    
    private lateinit var fileManager: FileManager
    private lateinit var notificationManager: NotificationManager
    
    private var screenWidth = 0
    private var screenHeight = 0
    private var screenDensity = 0
    
    private var audioSource = AudioSource.NONE
    private var videoQuality = VideoQuality.QUALITY_720P
    private var isPaused = false

    override fun onCreate() {
        super.onCreate()
        fileManager = FileManager(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        getScreenMetrics()
        
        Log.d(TAG, "RecordingService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START_RECORDING -> {
                val resultCode = intent.getIntExtra(Constants.EXTRA_RESULT_CODE, -1)
                val data = intent.getParcelableExtra<Intent>(Constants.EXTRA_RESULT_DATA)
                
                if (resultCode != -1 && data != null) {
                    startRecording(resultCode, data)
                }
            }
            Constants.ACTION_PAUSE_RECORDING -> pauseRecording()
            Constants.ACTION_RESUME_RECORDING -> resumeRecording()
            Constants.ACTION_STOP_RECORDING -> stopRecording()
        }
        
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun getScreenMetrics() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = display
            display?.getRealMetrics(metrics)
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getRealMetrics(metrics)
        }
        
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        screenDensity = metrics.densityDpi
        
        Log.d(TAG, "Screen metrics: ${screenWidth}x$screenHeight @ $screenDensity dpi")
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.notification_channel_description)
            setSound(null, null)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val mainIntent = Intent(this, MainActivity::class.java)
        val mainPendingIntent = PendingIntent.getActivity(
            this, 0, mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, RecordingService::class.java).apply {
            action = Constants.ACTION_STOP_RECORDING
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 1, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setOngoing(true)
            .setContentIntent(mainPendingIntent)
            .addAction(
                android.R.drawable.ic_media_pause,
                getString(R.string.notification_stop),
                stopPendingIntent
            )

        return builder.build()
    }

    private fun startRecording(resultCode: Int, data: Intent) {
        try {
            // Create output file
            outputFile = fileManager.createOutputFile()
            if (outputFile == null) {
                Log.e(TAG, "Failed to create output file")
                stopSelf()
                return
            }

            // Start foreground service with notification
            startForeground(Constants.NOTIFICATION_ID, createNotification())

            // Initialize MediaProjection
            val projectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            mediaProjection = projectionManager.getMediaProjection(resultCode, data)

            // Setup MediaRecorder
            setupMediaRecorder()

            // Create VirtualDisplay
            virtualDisplay = mediaProjection?.createVirtualDisplay(
                "ScreenRecorder",
                videoQuality.width,
                videoQuality.height,
                screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder?.surface,
                null,
                null
            )

            // Start recording
            mediaRecorder?.start()
            isRecording = true
            isPaused = false

            Log.d(TAG, "Recording started: ${outputFile?.absolutePath}")

        } catch (e: Exception) {
            Log.e(TAG, "Error starting recording", e)
            stopRecording()
        }
    }

    private fun setupMediaRecorder() {
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            // Set audio source first if needed
            if (audioSource != AudioSource.NONE) {
                setAudioSource(MediaRecorder.AudioSource.MIC)
            }
            
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(outputFile?.absolutePath)
            setVideoSize(videoQuality.width, videoQuality.height)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setVideoEncodingBitRate(videoQuality.bitrate)
            setVideoFrameRate(Constants.FRAME_RATE)
            
            if (audioSource != AudioSource.NONE) {
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(Constants.AUDIO_BIT_RATE)
                setAudioSamplingRate(Constants.AUDIO_SAMPLE_RATE)
                setAudioChannels(Constants.AUDIO_CHANNEL_COUNT)
            }

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(TAG, "Failed to prepare MediaRecorder", e)
                throw e
            }
        }
    }

    private fun pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                if (isRecording && !isPaused) {
                    mediaRecorder?.pause()
                    isPaused = true
                    Log.d(TAG, "Recording paused")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error pausing recording", e)
            }
        }
    }

    private fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                if (isRecording && isPaused) {
                    mediaRecorder?.resume()
                    isPaused = false
                    Log.d(TAG, "Recording resumed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error resuming recording", e)
            }
        }
    }

    private fun stopRecording() {
        try {
            isRecording = false
            isPaused = false

            mediaRecorder?.apply {
                try {
                    stop()
                } catch (e: Exception) {
                    Log.e(TAG, "Error stopping MediaRecorder", e)
                }
                reset()
                release()
            }
            mediaRecorder = null

            virtualDisplay?.release()
            virtualDisplay = null

            mediaProjection?.stop()
            mediaProjection = null

            Log.d(TAG, "Recording stopped: ${outputFile?.absolutePath}")

            // TODO: Show toast or notification that recording was saved
            
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping recording", e)
        } finally {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        Log.d(TAG, "RecordingService destroyed")
    }
}
