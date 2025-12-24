# Screen Recorder

A modern Android screen recorder app built with Jetpack Compose and Material 3.

## Features

- 🎥 Screen recording with MediaProjection API
- 🎤 Audio options: Microphone, Device Audio, or Both
- 📹 Video quality: 480p, 720p, 1080p
- ⏱️ 3-second countdown before recording
- 🎯 Floating overlay controls
- 💾 Auto-save to Movies/ScreenRecords
- 🔔 Recording notification

## Tech Stack

- **Kotlin 2.1.0** - [kotlinlang.org](https://kotlinlang.org/)
- **Jetpack Compose** - [developer.android.com/jetpack/compose](https://developer.android.com/jetpack/compose)
- **Material 3** - [m3.material.io](https://m3.material.io/)
- **Android 16 (API 36)** - [developer.android.com](https://developer.android.com/about/versions/16)
- **MVVM Architecture** - [Architecture Guide](https://developer.android.com/topic/architecture)

## Requirements

- Android Studio Ladybug (2024.2.1+)
- JDK 17
- Android SDK 36
- Min SDK: 26 (Android 8.0+)

## Build & Run

```bash
# Clone the repository
git clone https://github.com/techdiwas/screen-recorder.git
cd screen-recorder

# Build the project
./gradlew build

# Install on device
./gradlew installDebug
```

## Usage

1. Launch the app
2. Grant required permissions (Audio, Notifications, Overlay)
3. Select audio source and video quality
4. Tap "Start Recording"
5. Grant screen capture permission
6. Recording starts after 3-second countdown
7. Use floating controls to pause/stop
8. Find recordings in Movies/ScreenRecords

## Permissions

- `RECORD_AUDIO` - Microphone recording
- `FOREGROUND_SERVICE` - Background recording service
- `FOREGROUND_SERVICE_MEDIA_PROJECTION` - Screen capture
- `POST_NOTIFICATIONS` - Recording status notification
- `SYSTEM_ALERT_WINDOW` - Floating overlay controls

## Architecture

```
app/src/main/java/com/techdiwas/screenrecorder/
├── data/       # RecordingConfig, FileManager
├── domain/     # RecordingController
├── service/    # RecordingService (Foreground)
├── ui/         # Compose UI, ViewModels
└── util/       # Constants, Permissions
```

## Testing

```bash
# Run unit tests
./gradlew test
```

## CI/CD

GitHub Actions automatically builds and tests on push/PR. See `.github/workflows/android-ci.yml`

## License

MIT License - see [LICENSE](LICENSE) file

## References

- [Android Developer Docs](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [MediaProjection API](https://developer.android.com/reference/android/media/projection/MediaProjection)
