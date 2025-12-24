package com.techdiwas.screenrecorder.data

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Handles file operations for recordings.
 */
class FileManager(private val context: Context) {

    companion object {
        private const val RECORDINGS_DIR = "ScreenRecords"
        private const val FILE_PREFIX = "ScreenRecord_"
        private const val FILE_EXTENSION = ".mp4"
    }

    /**
     * Creates and returns the directory for storing recordings.
     * @return The recordings directory, or null if it cannot be created.
     */
    fun getRecordingsDirectory(): File? {
        val moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        val recordingsDir = File(moviesDir, RECORDINGS_DIR)
        
        return if (recordingsDir.exists() || recordingsDir.mkdirs()) {
            recordingsDir
        } else {
            // Fallback to app-specific directory if public directory fails
            val appDir = File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), RECORDINGS_DIR)
            if (appDir.exists() || appDir.mkdirs()) appDir else null
        }
    }

    /**
     * Generates a new output file for a recording.
     * @return A new File object, or null if directory cannot be created.
     */
    fun createOutputFile(): File? {
        val directory = getRecordingsDirectory() ?: return null
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "$FILE_PREFIX$timestamp$FILE_EXTENSION"
        return File(directory, fileName)
    }

    /**
     * Checks if external storage is writable.
     * @return True if writable, false otherwise.
     */
    fun isStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }
}
