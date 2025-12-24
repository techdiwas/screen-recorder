package com.techdiwas.screenrecorder.data

import java.io.File

/**
 * Configuration for a recording session.
 */
data class RecordingConfig(
    val audioSource: AudioSource = AudioSource.NONE,
    val videoQuality: VideoQuality = VideoQuality.QUALITY_720P,
    val outputFile: File? = null,
    val showTouches: Boolean = false
) {
    /**
     * Validates the configuration.
     * @return True if valid, false otherwise.
     */
    fun isValid(): Boolean {
        return outputFile != null && outputFile.parentFile?.exists() == true
    }
}
