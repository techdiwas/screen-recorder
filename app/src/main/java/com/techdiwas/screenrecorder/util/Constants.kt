package com.techdiwas.screenrecorder.util

/**
 * Constants used throughout the app.
 */
object Constants {
    // Notification
    const val NOTIFICATION_CHANNEL_ID = "screen_recording_channel"
    const val NOTIFICATION_ID = 1001
    
    // Intent Actions
    const val ACTION_START_RECORDING = "com.techdiwas.screenrecorder.START_RECORDING"
    const val ACTION_PAUSE_RECORDING = "com.techdiwas.screenrecorder.PAUSE_RECORDING"
    const val ACTION_RESUME_RECORDING = "com.techdiwas.screenrecorder.RESUME_RECORDING"
    const val ACTION_STOP_RECORDING = "com.techdiwas.screenrecorder.STOP_RECORDING"
    const val ACTION_SHOW_OVERLAY = "com.techdiwas.screenrecorder.SHOW_OVERLAY"
    
    // Intent Extras
    const val EXTRA_RESULT_CODE = "result_code"
    const val EXTRA_RESULT_DATA = "result_data"
    const val EXTRA_CONFIG = "config"
    
    // Recording
    const val FRAME_RATE = 30
    const val AUDIO_SAMPLE_RATE = 44100
    const val AUDIO_BIT_RATE = 128000
    const val AUDIO_CHANNEL_COUNT = 2
    
    // Countdown
    const val COUNTDOWN_SECONDS = 3
    const val COUNTDOWN_INTERVAL_MS = 1000L
    
    // Permissions
    const val PERMISSION_REQUEST_CODE = 100
    const val OVERLAY_PERMISSION_REQUEST_CODE = 200
}
