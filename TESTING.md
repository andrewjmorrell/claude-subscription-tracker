# Self Testing with Claude Code + ADB

## Purpose

This project uses an interactive, AI-assisted approach to UI testing. Instead of traditional automated UI test frameworks (Espresso, UI Automator), the app is tested through **Claude Code skills** that drive a real device or emulator via the **Android Debug Bridge (ADB) MCP tools**.

### Why this approach?

- **Guided and conversational**: Each test flow walks you through what's happening, explains *why* each step matters, and asks you to confirm results. This makes it easy to catch visual issues that automated assertions would miss (layout problems, color inconsistencies, text truncation).
- **Screenshot documentation**: Every significant step produces a named screenshot saved to a test folder, creating a visual record of each test run.
- **Low barrier to entry**: No test code to write or maintain. Just say "test dashboard" in Claude Code and the skill handles the rest.
- **Device-aware**: Each test automatically detects connected devices and emulators. If nothing is connected, it offers to start an emulator for you. If multiple devices are attached, it asks you to pick one.

### How it works

The tests are implemented as **Claude Code skills** (`.claude/skills/test-*/SKILL.md`). Each skill is a structured set of instructions that tells Claude how to:

1. **Detect devices** — Run `adb devices`, handle zero/one/multiple device scenarios, start an emulator if needed
2. **Launch the app** — Open `com.example.subscriptiontracker` via ADB
3. **Navigate and interact** — Use UI dumps to find element coordinates, tap buttons, type text, scroll
4. **Capture evidence** — Take screenshots at key moments with descriptive names
5. **Verify results** — Show you screenshots and ask for confirmation at each checkpoint

### Prerequisites

- **Claude Code** with the Android Debug Bridge MCP server configured
- A connected Android device or emulator (the tests will help you set one up if needed)
- The app installed on the device (`./gradlew installDebug`)

---

## Test 1: Dashboard (`test-dashboard`)

### What it tests

The Dashboard is the app's home screen and the first thing users see. This test verifies the core layout and navigation:

- **Empty state**: $0.00 totals, "No bills due" and "No active subscriptions" messages when no data exists
- **Summary cards**: Monthly cost, Yearly cost, and Active Subscriptions count are displayed correctly
- **Upcoming Bills section**: Shows bills due in the next 7 days with a "See all" link
- **Most Expensive section**: Shows top subscriptions ranked by monthly cost
- **Navigation**: The "Active Subscriptions" card navigates to the Subs tab; the "See all" link navigates to Upcoming Bills
- **Scrollability**: All content is accessible by scrolling

### How to run

```
test dashboard
```

or say: "test the home screen", "check the dashboard", "QA the dashboard"

---

## Test 2: Subscriptions (`test-subscriptions`)

### What it tests

The Subscriptions tab is the most feature-rich screen. This test covers the full CRUD lifecycle and all list interactions:

- **Empty state**: Shows "No subscriptions yet. Tap + to add one!" with a FAB
- **Add Subscription**: Fills in all form fields (name, amount, billing cycle, category, date), verifies the **sticky save button is always visible** (this was a past bug), and saves
- **Multiple subscriptions**: Adds a second subscription to verify the list handles multiple items
- **Edit flow**: Taps a subscription card, verifies pre-filled data, changes a value, and saves
- **Search**: Types a partial name and verifies the list filters
- **Sort**: Opens the sort dropdown and verifies reordering (by amount, name, date)
- **Category filter**: Filters by category and verifies the "Filtered: [category]" chip appears
- **Form validation**: Attempts to save with empty fields and verifies error messages appear

### How to run

```
test subscriptions
```

or say: "test the subs tab", "test adding a subscription", "test search and filter"

---

## Test 3: Upcoming Bills (`test-upcoming`)

### What it tests

The Upcoming Bills screen shows subscriptions due within 30 days. This test verifies:

- **Empty state**: Shows $0.00 total and "No upcoming bills in the next 30 days" when no bills are due
- **Total Due summary card**: Displays the correct total amount and bill count
- **Bill cards**: Each shows the subscription name, billing date, days-until text, and amount
- **Urgency highlighting**: Bills due within 3 days have their date text displayed in red (error color)
- **Scrollability**: Long lists of bills are scrollable

### How to run

```
test upcoming
```

or say: "test upcoming bills", "check the upcoming tab"

> **Note**: This test is most useful after adding subscriptions with near-future billing dates. If run on a fresh install, it will verify the empty state and suggest running the subscriptions test first.

---

## Test 4: Analytics (`test-analytics`)

### What it tests

The Analytics screen provides spending analysis across all active subscriptions. This test verifies:

- **Cost summary cards**: Monthly total, Yearly total, and Daily Average are displayed and mathematically consistent (daily ~ yearly / 365)
- **Category breakdown**: Each category shows its name, monthly cost, a proportional progress bar, percentage of total, and subscription count
- **Percentages**: The progress bars visually match the displayed percentages, and the top category has the longest bar
- **Summary section**: Active subscription count, categories used, and average cost per subscription
- **Scrollability**: All content from cost cards to summary section is accessible

### How to run

```
test analytics
```

or say: "test the analytics tab", "check spending breakdown", "test category breakdown"

> **Note**: Like the Upcoming test, this is most informative with existing subscription data.

---

## Test 5: Settings (`test-settings`)

### What it tests

The Settings screen controls app-wide preferences. This test verifies each setting works and its effects propagate correctly:

- **Dark Mode toggle**: Switches the entire app theme immediately. The test toggles it on, screenshots the dark theme, then toggles it back off and verifies the theme reverts
- **Currency change**: Changes the currency symbol (e.g., $ to €) and navigates to the Dashboard to verify the new symbol appears in all monetary displays
- **Reminders toggle**: Turning reminders off hides the "Remind me" timing dropdown; turning them back on makes it reappear
- **Reminder timing dropdown**: Opens the dropdown and selects a different timing option (Same day, 1 day before, 3 days before)
- **About section**: Verifies "Subscription Tracker" and "Version 1.0" are displayed

### How to run

```
test settings
```

or say: "test the settings tab", "test dark mode", "test currency change"

---

## Test Artifacts

Each test run creates a folder of timestamped screenshots. The naming convention is:

```
[test-name]/
├── 00_emulator_ready        # (if an emulator was started)
├── 01_[screen]_initial
├── 02_[action_result]
├── 03_[next_action]
└── ...
```

These screenshots serve as a visual test report and can be reviewed after the test completes.

---

## Running All Tests

To test the complete app, run the test skills in this recommended order:

1. **`test dashboard`** — Verify the app launches and the home screen renders
2. **`test subscriptions`** — Add test data and verify all CRUD operations
3. **`test upcoming`** — Verify upcoming bills with the data created in step 2
4. **`test analytics`** — Verify spending analysis with the same data
5. **`test settings`** — Verify preferences and their app-wide effects

This order ensures each subsequent test has data to work with.
