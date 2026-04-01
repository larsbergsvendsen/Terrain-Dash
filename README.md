# Terrain Dash

A fast-paced, physics-based side-scrolling racing game for Android, inspired by Hill Climb Racing but with higher speed, more action, and a punchy visual style.

## Concept

Control a vehicle racing across wild terrain — balance it with touch controls to avoid flipping, collect coins, activate nitro boosts, and race through 30 handcrafted levels across 6 unique worlds.

## Tech Stack

- **Engine:** LibGDX 1.12+
- **Physics:** Box2D
- **Language:** Java (core) + Kotlin (Android)
- **Build:** Gradle (Kotlin DSL)
- **Target:** Android 7.0+ (API 24)

## Project Structure

```
terrain-dash/
├── core/       → Platform-independent game logic
├── android/    → Android launcher
├── desktop/    → Desktop launcher (for development)
├── assets/     → Shared game assets
└── docs/       → Game Design Document & Architecture
```

## Documentation

- [Game Design Document](docs/GDD.md) — Full game design with mechanics, worlds, vehicles, and economy
- [Architecture](docs/ARCHITECTURE.md) — Technical architecture, class diagrams, and rendering pipeline

## Building

```bash
# Desktop (for testing)
./gradlew desktop:run

# Android APK
./gradlew android:assembleDebug
```

## License

Proprietary — All rights reserved.
