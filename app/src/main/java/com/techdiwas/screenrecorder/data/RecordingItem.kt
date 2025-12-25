package com.techdiwas.screenrecorder.data

import java.io.File
import java.util.Date

/**
 * Represents a recorded video file.
 */
data class RecordingItem(
    val file: File,
    val name: String,
    val dateModified: Date,
    val sizeBytes: Long,
    val durationMs: Long = 0L
) {
    /**
     * Returns a human-readable file size.
     */
    fun getFormattedSize(): String {
        val kb = sizeBytes / 1024.0
        val mb = kb / 1024.0
        return when {
            mb >= 1.0 -> String.format("%.1f MB", mb)
            kb >= 1.0 -> String.format("%.1f KB", kb)
            else -> "$sizeBytes B"
        }
    }
}
