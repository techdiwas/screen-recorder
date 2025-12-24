package com.techdiwas.screenrecorder.data

/**
 * Audio source options for screen recording.
 */
enum class AudioSource {
    NONE,           // No audio
    MICROPHONE,     // Microphone only
    DEVICE,         // Device/internal audio (Android 10+)
    BOTH            // Microphone + Device audio
}

/**
 * Video quality/resolution options.
 */
enum class VideoQuality(
    val width: Int,
    val height: Int,
    val bitrate: Int,
    val displayName: String
) {
    QUALITY_480P(854, 480, 2_500_000, "480p (SD)"),
    QUALITY_720P(1280, 720, 5_000_000, "720p (HD)"),
    QUALITY_1080P(1920, 1080, 10_000_000, "1080p (Full HD)")
}

/**
 * Recording state.
 */
enum class RecordingState {
    IDLE,
    COUNTDOWN,
    RECORDING,
    PAUSED,
    STOPPING
}
