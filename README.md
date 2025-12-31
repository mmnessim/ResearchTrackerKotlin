# ResearchTrackerKMP

A small multiplatform app for tracking RSS articles related to user-defined search terms.
The project includes a Kotlin Multiplatform Compose client (Android + iOS + JVM)
and a Ktor-based backend service that aggregates RSS feeds.

## Note from the Creator

This is a relatively small project that is my first foray into Kotlin Multiplatform. I am a hobby
developer,
but I ultimately would love to have this app released for both iOS and Android. This is very much a
work
in progress and a learning project.

# Overview

- The app lets users add search terms, fetches recent articles from many sources, and surfaces new
  content and notifications when relevant.
- Backend: a Ktor service that aggregates ~150 RSS feeds (and growing) and exposes a simple search
  API used by the app.
- Platforms: Android and iOS clients built with Kotlin Multiplatform and Jetpack Compose / Compose
  for iOS.

# Planned Additions

- Many more RSS feeds
- Options screen to manage notifications and other settings
- Bring-your-own RSS feeds feature
- BGTaskScheduler for iOS
- UI improvements
- Eventual release for iOS and Android
- CI/CD
- Publicly hosted backend, plus open sourcing of backend repo

# Key features

- Add / remove / lock tracked terms.
- Aggregated RSS search across many sources (backend service).
- Article list with navigation to a details screen.
- Local persistence via SQLDelight.
- Dependency injection with Koin.
- Background scheduling on Android (WorkManager) and planned background work on iOS (
  BGTaskScheduler).
- Notification plumbing on both platforms (platform-specific implementations).

# Architecture & tech

- Kotlin Multiplatform (shared business logic in `commonMain`).
- Compose Multiplatform for UI (`composeApp` module).
- Ktor client for network calls (shared HTTP clients provided per-platform).
- SQLDelight for local storage and repository abstractions.
- Koin for dependency injection.
- WorkManager on Android for background work; iOS uses platform-specific background APIs via
  expect/actual.

# Repository layout

- `composeApp/` - multiplatform Compose UI and shared client logic.
- `iosApp/` - iOS app shell and platform wiring.
- `composeApp/src/commonMain` - shared viewmodels, models, services, and UI code.
- `backend/` (if present) - Ktor backend that aggregates RSS feeds and serves the search endpoint.

# Build & run (quick)

- Requirements: JDK 17+, Android SDK, Xcode (for iOS simulator builds), Gradle (wrapper provided).
- To build Android:
    - Open the project in Android Studio, select the `composeApp` module, and run the Android
      target (or use Gradle wrapper: `./gradlew :composeApp:installDebug`).
- To build iOS (Simulator):
    - Open `iosApp/iosApp.xcodeproj` or `iosApp/iosApp.xcworkspace` in Xcode and run the `iosApp`
      scheme (or export frameworks using Gradle if you prefer command line).

# Configuration & environment

- Backend URL is set in `composeApp/src/commonMain/ConfigFlags.kt`. It is set to a locally running
  backend server
  on my personal Tailnet. At the moment that repo is private, but I will publish it eventually. At
  that point you
  will be able to host the backend yourself for testing/local deployment
- On Android, cleartext (HTTP) is blocked by default; update `networkSecurityConfig` or use HTTPS
  for backend calls during development.

# Testing

At this point there are a few UI tests, with more comprehensive testing planned.

# Contributing

- Fork the repo and open a PR with a clear description of the change and why it’s needed.
- Keep shared business logic in `commonMain` whenever possible.

# Contact / author

Currently a solo project. For issues, suggestions, notes, or anything else, contact me at
mnessimdev@gmail.com