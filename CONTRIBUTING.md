# Contributing to Screen Recorder

Thank you for your interest in contributing to the Screen Recorder project! This document provides guidelines and instructions for contributing.

## Getting Started

1. **Fork the repository**
2. **Clone your fork**
   ```bash
   git clone https://github.com/YOUR_USERNAME/screen-recorder.git
   cd screen-recorder
   ```
3. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Development Setup

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Git

### Building the Project
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Running on Device/Emulator
```bash
./gradlew installDebug
```

## Code Style

This project follows the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).

### Key Points
- Use 4 spaces for indentation
- Use camelCase for functions and variables
- Use PascalCase for classes
- Keep lines under 120 characters when possible
- Add KDoc comments for public APIs
- Use meaningful variable and function names

### Example
```kotlin
/**
 * Starts a new recording session with the given configuration.
 *
 * @param config The recording configuration
 * @return True if recording started successfully, false otherwise
 */
fun startRecording(config: RecordingConfig): Boolean {
    // Implementation
}
```

## Project Structure

```
app/src/main/java/com/techdiwas/screenrecorder/
├── data/       # Data models and file handling
├── domain/     # Business logic and controllers
├── service/    # Background services
├── ui/         # UI components (Compose)
└── util/       # Utilities and helpers
```

## Making Changes

### 1. Find or Create an Issue
- Check existing issues before starting work
- Create a new issue if needed
- Discuss major changes in issues before implementing

### 2. Write Code
- Follow the existing code style
- Keep changes focused and minimal
- Add comments for complex logic
- Update documentation if needed

### 3. Write Tests
- Add unit tests for new functionality
- Update existing tests if behavior changes
- Ensure all tests pass before submitting

### 4. Test Thoroughly
- Test on real Android device (emulator may have limitations)
- Test different Android versions if possible
- Test edge cases and error conditions

### 5. Commit Changes
```bash
git add .
git commit -m "feat: add feature description"
```

Use conventional commit messages:
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation changes
- `style:` Code style changes (formatting)
- `refactor:` Code refactoring
- `test:` Adding or updating tests
- `chore:` Maintenance tasks

### 6. Push and Create PR
```bash
git push origin feature/your-feature-name
```

Then create a Pull Request on GitHub with:
- Clear title and description
- Reference related issues
- Screenshots for UI changes
- Testing instructions

## Pull Request Guidelines

### Before Submitting
- [ ] Code follows the project style
- [ ] All tests pass
- [ ] New tests added for new features
- [ ] Documentation updated if needed
- [ ] Commit messages are clear and descriptive
- [ ] No merge conflicts

### PR Description Should Include
- What changes were made
- Why the changes were needed
- How to test the changes
- Screenshots (for UI changes)
- Related issue numbers

## Testing Guidelines

### Unit Tests
- Test business logic and data models
- Use JUnit 4 for assertions
- Mock Android framework components when needed
- Keep tests fast and isolated

### Integration Tests
- Test interactions between components
- Require Android device/emulator
- Test real-world scenarios

### UI Tests
- Test user interactions
- Use Compose testing APIs
- Test different screen sizes

## Areas for Contribution

### High Priority
- [ ] Implement proper device audio capture
- [ ] Add audio mixing support (mic + device)
- [ ] Create draggable overlay controls
- [ ] Add video preview after recording
- [ ] Improve error handling and user feedback

### Medium Priority
- [ ] Add settings screen
- [ ] Implement video trimming
- [ ] Add recording history/gallery
- [ ] Support landscape orientation
- [ ] Add touch indicator overlay

### Low Priority
- [ ] Implement video sharing
- [ ] Add face camera overlay
- [ ] Customize countdown duration
- [ ] Add themes/customization
- [ ] Localization support

## Bug Reports

When reporting bugs, include:
- Android version
- Device model
- Steps to reproduce
- Expected behavior
- Actual behavior
- Screenshots/logs if applicable

## Feature Requests

For feature requests, include:
- Clear description of the feature
- Use cases and benefits
- Potential implementation approach
- Examples from other apps (if applicable)

## Code Review Process

1. Maintainers will review your PR
2. Address any feedback or requested changes
3. Once approved, your PR will be merged
4. Your contribution will be included in the next release

## Questions?

- Open an issue for questions
- Check existing documentation
- Review closed issues for similar questions

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

## Code of Conduct

- Be respectful and professional
- Welcome newcomers
- Focus on constructive feedback
- Help others learn and grow

Thank you for contributing! 🎉
