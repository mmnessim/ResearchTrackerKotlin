# Release Notes

## Upcoming Release - Version 1.0.7

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

