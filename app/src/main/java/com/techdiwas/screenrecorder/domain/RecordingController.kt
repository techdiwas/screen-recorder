package com.techdiwas.screenrecorder.domain

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import com.techdiwas.screenrecorder.data.RecordingConfig
import com.techdiwas.screenrecorder.service.RecordingService
import com.techdiwas.screenrecorder.util.Constants

/**
 * Controller for managing recording operations.
 * Coordinates between UI and RecordingService.
 */
class RecordingController(private val context: Context) {

    private val mediaProjectionManager: MediaProjectionManager =
        context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

    /**
     * Gets an intent to request media projection permission.
     * This intent should be launched with startActivityForResult.
     */
    fun getMediaProjectionIntent(): Intent {
        return mediaProjectionManager.createScreenCaptureIntent()
    }

    /**
     * Starts the recording service with the given configuration.
     * 
     * @param resultCode Result code from MediaProjection permission request
     * @param data Intent data from MediaProjection permission request
     * @param config Recording configuration
     */
    fun startRecording(resultCode: Int, data: Intent, config: RecordingConfig) {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = Constants.ACTION_START_RECORDING
            putExtra(Constants.EXTRA_RESULT_CODE, resultCode)
            putExtra(Constants.EXTRA_RESULT_DATA, data)
            // Note: RecordingConfig needs to be Parcelable for this to work in production
            // For now, we'll pass individual values in the service
        }
        context.startForegroundService(intent)
    }

    /**
     * Pauses the current recording.
     */
    fun pauseRecording() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = Constants.ACTION_PAUSE_RECORDING
        }
        context.startService(intent)
    }

    /**
     * Resumes a paused recording.
     */
    fun resumeRecording() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = Constants.ACTION_RESUME_RECORDING
        }
        context.startService(intent)
    }

    /**
     * Stops the current recording.
     */
    fun stopRecording() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = Constants.ACTION_STOP_RECORDING
        }
        context.startService(intent)
    }

    /**
     * Shows the floating overlay controls.
     */
    fun showOverlay() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = Constants.ACTION_SHOW_OVERLAY
        }
        context.startService(intent)
    }
}
