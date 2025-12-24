# Screen Recorder

A Google-style Android Screen Recorder application built with modern Android development practices.

## Features

- ✅ **Screen recording** using MediaProjection API
- 🎤 **Multiple audio sources**:
  - No audio
  - Microphone only
  - Device audio (Android 10+)
  - Microphone + Device audio
- 🎥 **Video quality options**:
  - 480p (SD)
  - 720p (HD)
  - 1080p (Full HD)
- ⏱️ **3-second countdown** before recording starts
- 🎯 **Floating overlay controls** (pause/resume/stop)
- 🔔 **System notification** while recording
- 💾 **Auto-save** to Movies/ScreenRecords
- 🔐 **Proper permission handling**
- 🔄 **Graceful orientation changes**

## Tech Stack

- **Language**: Kotlin
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)
- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Build System**: Gradle (Kotlin DSL)
- **CI/CD**: GitHub Actions

## Project Structure

```
app/src/main/java/com/techdiwas/screenrecorder/
├── data/               # Data models and file handling
│   ├── AudioSource.kt
│   ├── RecordingConfig.kt
│   ├── RecordingEnums.kt
│   └── FileManager.kt
├── domain/            # Business logic and controllers
│   └── RecordingController.kt
├── service/           # Foreground recording service
│   └── RecordingService.kt
├── ui/                # UI components (Compose)
│   ├── MainActivity.kt
│   ├── MainViewModel.kt
│   ├── OverlayActivity.kt
│   └── theme/
└── util/              # Utilities and helpers
    ├── Constants.kt
    └── PermissionHelper.kt
```

## Building the Project

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/techdiwas/screen-recorder.git
   cd screen-recorder
   ```

2. Open the project in Android Studio

3. Sync Gradle and build:
   ```bash
   ./gradlew build
   ```

4. Run tests:
   ```bash
   ./gradlew test
   ```

## Running the App

1. Connect an Android device (API 26+) or start an emulator
2. Click **Run** in Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

### Required Permissions

The app will request the following permissions at runtime:

- **Record Audio** - For capturing microphone audio
- **Post Notifications** (Android 13+) - For showing recording status
- **System Alert Window** - For floating overlay controls
- **Write External Storage** (Android 8-9) - For saving recordings

## Usage

1. **Launch the app** and grant the requested permissions
2. **Select audio source** (None, Microphone, Device, or Both)
3. **Choose video quality** (480p, 720p, or 1080p)
4. **Tap "Start Recording"**
5. Grant **screen capture permission** when prompted
6. After a **3-second countdown**, recording begins
7. Use the **floating controls** to pause/resume or stop
8. Recordings are saved to **Movies/ScreenRecords**

## CI/CD

GitHub Actions workflow automatically:
- Builds the project on push/PR
- Runs unit tests
- Caches Gradle dependencies
- Uploads build artifacts

See [`.github/workflows/android-ci.yml`](.github/workflows/android-ci.yml)

## Testing

Basic unit tests are included for:
- `RecordingConfig` validation logic
- `RecordingEnums` value checks

Run tests:
```bash
./gradlew test
```

## TODO / Known Limitations

- [ ] **Device audio recording** requires Android 10+ and proper audio capture setup
- [ ] **Pause/Resume** functionality requires Android 7.0+ (API 24+)
- [ ] **Overlay controls** currently use a transparent activity; consider using WindowManager for true draggable overlay
- [ ] **Audio mixing** (mic + device) needs careful MediaRecorder configuration
- [ ] **Real device testing** required for full functionality verification
- [ ] Add **video preview** after recording
- [ ] Implement **settings screen** for bitrate/framerate customization
- [ ] Add **video trimming** feature
- [ ] Support **landscape mode** recording

## Notes for Real Device Testing

1. **Emulator limitations**: Screen recording works best on real devices. Emulators may have issues with MediaProjection or audio capture.

2. **Device audio**: Internal audio capture requires Android 10+ and may not work on all devices due to vendor restrictions.

3. **Storage access**: On Android 11+, scoped storage is used. Files are saved to the public Movies directory with proper media store updates.

4. **Overlay permission**: Must be granted manually from Settings on most devices.

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Built following Android best practices and Material Design guidelines
- Uses MediaProjection API (introduced in Android 5.0)
- Inspired by Google's built-in screen recorder on Pixel devices

---

**Note**: This app does NOT use proprietary Google code or undocumented APIs. All functionality is implemented using public Android APIs available to all developers.
