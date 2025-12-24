package com.techdiwas.screenrecorder.data

import org.junit.Assert.*
import org.junit.Test
import java.io.File

/**
 * Unit tests for RecordingConfig.
 */
class RecordingConfigTest {

    @Test
    fun `config with null output file is invalid`() {
        val config = RecordingConfig(
            audioSource = AudioSource.MICROPHONE,
            videoQuality = VideoQuality.QUALITY_720P,
            outputFile = null
        )
        
        assertFalse(config.isValid())
    }

    @Test
    fun `config with non-existent parent directory is invalid`() {
        val file = File("/non/existent/path/output.mp4")
        val config = RecordingConfig(
            audioSource = AudioSource.MICROPHONE,
            videoQuality = VideoQuality.QUALITY_720P,
            outputFile = file
        )
        
        assertFalse(config.isValid())
    }

    @Test
    fun `config with valid file path is valid`() {
        // Create a temp file for testing
        val tempFile = File.createTempFile("test", ".mp4")
        tempFile.deleteOnExit()
        
        val config = RecordingConfig(
            audioSource = AudioSource.NONE,
            videoQuality = VideoQuality.QUALITY_1080P,
            outputFile = tempFile
        )
        
        assertTrue(config.isValid())
    }

    @Test
    fun `default config values are correct`() {
        val config = RecordingConfig()
        
        assertEquals(AudioSource.NONE, config.audioSource)
        assertEquals(VideoQuality.QUALITY_720P, config.videoQuality)
        assertNull(config.outputFile)
        assertFalse(config.showTouches)
    }
}
