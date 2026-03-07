# QuickClock - Native Wear OS Work Time Tracker

A native Android Wear OS 5 app for tracking work hours on Samsung Galaxy Watch 8.

## Features

- Quick check-in/check-out time tracking
- Automatic work duration calculation
- CSV export of work sessions
- Kotlin + Jetpack Compose UI optimized for round displays
- Persistent local storage using DataStore

## Tech Stack

- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Wear Compose** - Wear OS specific Compose library
- **DataStore Preferences** - Local data persistence
- **Android minSDK 26** - Wear OS 5+

## Build & Deploy

### Prerequisites
- Android SDK 34 (API level 34)
- minSDK 26 (Wear OS 5)
- Java 21
- Gradle 8.10.2

### Build
```bash
cd android
./gradlew clean build
```

### Deploy to Watch
```bash
adb install -r android/app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n "com.quickclock.app/.MainActivity"
```

## Project Structure

```
android/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/quickclock/app/
│   │   │   ├── MainActivity.kt
│   │   │   ├── model/Worktime.kt
│   │   │   ├── viewmodel/WorktimeViewModel.kt
│   │   │   ├── data/WorktimeRepository.kt
│   │   │   ├── data/WorktimeDataStore.kt
│   │   │   └── ui/
│   │   │       ├── theme/
│   │   │       ├── screens/
│   │   │       └── components/
│   │   ├── res/
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   └── colors.xml
│   │   │   └── mipmap/ (launcher icons)
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── local.properties (local only)
```

## License

MIT
