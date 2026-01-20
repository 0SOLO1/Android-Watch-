# mf_watch

A short overview for the mf_watch demo project.

## Project overview

mf_watch is a mobile demo project contained in this repository. It provides a small reference application that demonstrates watch-style UI/feature patterns and related app architecture. The project is organized as an Android Gradle project with an `app/` module.

## Key features
- Demo watch UI screens and flows
- Example build and packaging via Gradle wrapper
- Ready-to-run Android module in `app/`

## Repository structure (top-level)
- `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties` — top-level Gradle configuration
- `gradle/` — version catalogs and wrapper
- `app/` — main Android application module
  - `src/` — application source code and resources
  - `build/` — generated build artifacts (do not commit)
  - `lint.xml`, `proguard-rules.pro` — linting and ProGuard rules

## Prerequisites
- JDK 11+ (match project's Gradle/JDK requirements)
- Android SDK and command-line tools
- Android Studio (recommended) or use the Gradle wrapper from a terminal

If this is a Flutter project, also install:
- Flutter SDK (stable channel)
- Dart SDK (bundled with Flutter)

## Build & run (Android native)
Open the project in Android Studio or use the Gradle wrapper from the project root.

From the project root (macOS / Linux / zsh):

```bash
# Build debug APK
./gradlew assembleDebug

# Install debug build to connected device/emulator
./gradlew installDebug
```

To run from Android Studio: File → Open, choose the project root and run the `app` configuration.

## Build & run (Flutter — only if this is a Flutter app)
If the project is a Flutter project, use the Flutter toolchain instead:

```bash
# Get dependencies
flutter pub get

# Run on connected device
flutter run
```

Tell me if you want me to add exact Flutter instructions and CI steps.

## Testing
- Unit and instrumentation tests (if present) can be run with Gradle:

```bash
# Run unit tests
./gradlew test

# Run instrumentation tests (on device/emulator)
./gradlew connectedAndroidTest
```

## Lint and formatting
- Android lint: `./gradlew lint`

## Contributing
- Fork the repo, create a feature branch, and open a pull request with a concise description of your changes.
- Keep generated build files out of commits.

## Notes & TODOs
- Confirm whether this is a Flutter app or an Android-only app so I can add accurate Flutter instructions and badges.
- Add CI configuration (GitHub Actions) to build and run tests on PRs.

## License
Add a LICENSE file to the repo and update this section with the chosen license.

## Contact
For questions, open an issue or contact the maintainers via the repository's issue tracker.
