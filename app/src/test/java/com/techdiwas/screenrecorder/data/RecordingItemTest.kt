package com.techdiwas.screenrecorder.data

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.util.Date

/**
 * Unit tests for RecordingItem.
 */
class RecordingItemTest {

    @Test
    fun `getFormattedSize returns bytes for small files`() {
        val recording = createRecordingItem(sizeBytes = 500)
        assertEquals("500 B", recording.getFormattedSize())
    }

    @Test
    fun `getFormattedSize returns KB for kilobyte-sized files`() {
        val recording = createRecordingItem(sizeBytes = 2048)
        assertEquals("2.0 KB", recording.getFormattedSize())
    }

    @Test
    fun `getFormattedSize returns MB for megabyte-sized files`() {
        val recording = createRecordingItem(sizeBytes = 5_242_880) // 5 MB
        assertEquals("5.0 MB", recording.getFormattedSize())
    }

    @Test
    fun `getFormattedSize handles large files correctly`() {
        val recording = createRecordingItem(sizeBytes = 104_857_600) // 100 MB
        assertEquals("100.0 MB", recording.getFormattedSize())
    }

    @Test
    fun `getFormattedSize handles zero bytes`() {
        val recording = createRecordingItem(sizeBytes = 0)
        assertEquals("0 B", recording.getFormattedSize())
    }

    @Test
    fun `recording item stores correct data`() {
        val file = File("/test/path/video.mp4")
        val date = Date()
        val recording = RecordingItem(
            file = file,
            name = "TestVideo",
            dateModified = date,
            sizeBytes = 1024,
            durationMs = 5000
        )

        assertEquals(file, recording.file)
        assertEquals("TestVideo", recording.name)
        assertEquals(date, recording.dateModified)
        assertEquals(1024, recording.sizeBytes)
        assertEquals(5000, recording.durationMs)
    }

    private fun createRecordingItem(sizeBytes: Long): RecordingItem {
        return RecordingItem(
            file = File("/test/path/video.mp4"),
            name = "TestVideo",
            dateModified = Date(),
            sizeBytes = sizeBytes
        )
    }
}
