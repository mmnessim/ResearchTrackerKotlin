# Release Notes

## Upcoming Release - Version 1.0.10

### Features

- Changed "Terms" tile to "News" for clarity
- Added styling to "About" screen
- Added message to "News" screen if no terms are added
- Updated backend to more sophisticated search (using Meilisearch)

## Previous Release - Version 1.0.9

### Features

- Indicator for terms with new results
- Automatically checks each term for new results when visiting terms list
- TODO: Improve design and information in About screen
- Fully migrate to Rust backend, remove old backend (should have slightly better performance and be
  more stable)

### Bug fixes:

- Fix tapping notifications not routing to correct Details screen
- Fix crashing on iOS when rotating to landscape
- Mitigate error when launching from notification on Android, navigate to home screen instead of
  error

### iOS Rework:

Due to iOS handling of background tasks, I have reworked the notification flow on IOS. On Android,
the app checks for new results every 15 minutes by default, but you can adjust the interval. On iOS,
background tasks are extremely unreliable, so users may never actually get any notifications. The
new
iOS approach is to schedule reminder notifications periodically to launch the app. Once the app is
launched,
it can check for updates reliably.

It's a less than ideal solution, and I may adjust in the future to a more robust solution, but it
should work for now.

## Previous Release - Version 1.0.8

### Features:

- Save articles persistently and view all saved articles

### Bug fixes:

- Fix/implement scrolling on terms screen
- Probably fixed an issue where adjusting refresh interval causes a crash
- Fixed issue with multiple notifications not displaying properly on Android

## Previous Release - Version 1.0.7

### Features:

- About screen with version info and dev contact info

### Minor tweaks:

- Pruned some fields from Article to improve performance
  and reliability of deserializing API
  results

### Experimental:

- Toggle switch in About screen to use experimental Rust backend
- Rust backend features better datetime handling, limit of 100 results, html sanitizing/removal

## Previous Release - Version 1.0.6 - January 4, 2026

Released to internal testers on Android and iOS

### Features:

- Health check indicator -> tap to refresh
- Truncate long descriptions in articles
- Options screen [IN PROGRESS]
- Adjust notification/refresh interval [TODO]

### Bug fixes:

- Fixed terms showing no results because of blank field
- Set permissions for iOS background fetch and background processing

### Sources:

- Added many new RSS feeds to backend

