package com.techdiwas.screenrecorder.data

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for recording enums.
 */
class RecordingEnumsTest {

    @Test
    fun `audio source enum has correct values`() {
        val sources = AudioSource.entries
        
        assertEquals(4, sources.size)
        assertTrue(sources.contains(AudioSource.NONE))
        assertTrue(sources.contains(AudioSource.MICROPHONE))
        assertTrue(sources.contains(AudioSource.DEVICE))
        assertTrue(sources.contains(AudioSource.BOTH))
    }

    @Test
    fun `video quality 720p has correct dimensions`() {
        val quality = VideoQuality.QUALITY_720P
        
        assertEquals(1280, quality.width)
        assertEquals(720, quality.height)
        assertEquals(5_000_000, quality.bitrate)
        assertEquals("720p (HD)", quality.displayName)
    }

    @Test
    fun `video quality 1080p has correct dimensions`() {
        val quality = VideoQuality.QUALITY_1080P
        
        assertEquals(1920, quality.width)
        assertEquals(1080, quality.height)
        assertEquals(10_000_000, quality.bitrate)
        assertEquals("1080p (Full HD)", quality.displayName)
    }

    @Test
    fun `video quality 480p has correct dimensions`() {
        val quality = VideoQuality.QUALITY_480P
        
        assertEquals(854, quality.width)
        assertEquals(480, quality.height)
        assertEquals(2_500_000, quality.bitrate)
        assertEquals("480p (SD)", quality.displayName)
    }

    @Test
    fun `recording state enum has all states`() {
        val states = RecordingState.entries
        
        assertEquals(5, states.size)
        assertTrue(states.contains(RecordingState.IDLE))
        assertTrue(states.contains(RecordingState.COUNTDOWN))
        assertTrue(states.contains(RecordingState.RECORDING))
        assertTrue(states.contains(RecordingState.PAUSED))
        assertTrue(states.contains(RecordingState.STOPPING))
    }
}
