# Subscription Tracker

An Android app for tracking recurring subscriptions and understanding monthly and yearly spending. Built with modern Android development practices.

## Features

- **Dashboard** — Total monthly/yearly cost, active subscription count, upcoming bills, highest cost subscriptions
- **Subscription List** — Full list with search by name, filter by category, sort by name/amount/billing date, swipe to delete with undo, tap to edit
- **Add/Edit Subscription** — Name, amount, billing cycle, category, next billing date, notes, trial end date, active toggle
- **Upcoming Bills** — Bills due within 30 days with urgency highlighting and total due summary
- **Analytics** — Category spending breakdown with progress bars, monthly/yearly/daily totals, average per subscription
- **Settings** — Currency symbol, reminder timing preference, dark mode toggle, reminders on/off
- **Reminders** — Local notifications for upcoming billing dates (same day / 1 day / 3 days before)

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Repository pattern |
| DI | Hilt |
| Database | Room |
| Preferences | DataStore |
| Async | Coroutines + Flow / StateFlow |
| Navigation | Navigation Compose |
| Reminders | WorkManager |
| Testing | JUnit + MockK + Turbine |

## Project Structure

```
app/src/main/java/com/example/subscriptiontracker/
├── data/
│   ├── local/
│   │   ├── converter/     # Room type converters
│   │   ├── dao/           # Data access objects
│   │   ├── entity/        # Room entities
│   │   └── AppDatabase.kt
│   └── repository/        # Repository implementations
├── di/                    # Hilt DI modules
├── domain/
│   ├── model/             # Domain models and enums
│   └── repository/        # Repository interfaces
├── ui/
│   ├── component/         # Shared UI components (SubscriptionCard)
│   ├── navigation/        # Navigation graph, routes, bottom nav items
│   ├── screen/
│   │   ├── addedit/       # Add/Edit subscription screen
│   │   ├── analytics/     # Analytics screen
│   │   ├── dashboard/     # Dashboard screen
│   │   ├── settings/      # Settings screen
│   │   ├── subscriptionlist/ # Subscription list screen
│   │   └── upcoming/      # Upcoming bills screen
│   └── theme/             # Material 3 theme
├── util/                  # Business logic helpers and format utilities
├── worker/                # WorkManager reminder worker and scheduler
├── MainActivity.kt
└── SubscriptionApp.kt
```

## Setup

1. Clone the repository
2. Open in Android Studio (Hedgehog or later recommended)
3. Sync Gradle
4. Run on an emulator or device (minSdk 26 / Android 8.0+)

## Architecture

The app follows **MVVM** with a **clean architecture** approach:

- **Domain layer** — Pure Kotlin models, repository interfaces. No Android dependencies.
- **Data layer** — Room database with entities, DAO, type converters. DataStore for preferences. Repository implementations.
- **UI layer** — Compose screens with per-screen ViewModels. State exposed via `StateFlow`, collected with `collectAsStateWithLifecycle`.
- **Util layer** — Business logic helpers (totals, filtering, sorting, validation) and formatting utilities.
- **DI** — Hilt modules bind repositories and provide database/DAO instances.

All data is local-first and offline-capable. No backend or cloud dependencies.

## Testing

Unit tests cover:
- Billing cycle conversions (monthly/yearly normalization)
- Subscription domain model computed properties (monthlyCost, yearlyCost, isInTrial, daysUntilNextBilling)
- Helper functions (totals, category spending, filtering, sorting, validation)
- Format utilities (currency, dates, days until)

Run tests:
```bash
./gradlew test
```

## Self Testing

This project includes interactive, AI-assisted UI testing using Claude Code skills and the Android Debug Bridge (ADB). See **[TESTING.md](TESTING.md)** for full details on the testing approach, available test flows, and how to run them.

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- minSdk 26 (Android 8.0)
