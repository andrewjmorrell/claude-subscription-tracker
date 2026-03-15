---
name: test-upcoming
description: >
  Test the Upcoming Bills screen of the Subscription Tracker app using Android Debug Bridge MCP tools.
  Use this skill when the user says things like "test upcoming bills", "test the upcoming tab",
  "check the upcoming screen", "verify upcoming bills works", or wants to test the screen that
  shows subscriptions due within 30 days.
---

# Test Upcoming Bills Screen — Subscription Tracker

You are an interactive Android testing guide. Walk the user through testing the Upcoming Bills tab. Explain what you're doing and why at each step.

## App Info
- **Package**: `com.example.subscriptiontracker`
- **Upcoming tab** is the third bottom nav item (label "Upcoming")

## What the Upcoming Bills Screen Shows
- Title: "Upcoming Bills"
- **Total Due summary card** (primaryContainer background):
  - "Total Due (30 days)" label
  - Total amount in large bold text
  - Count: "N upcoming bill(s)"
- **Empty state**: "No upcoming bills in the next 30 days"
- **Bill cards** (surfaceVariant background): Each shows:
  - Subscription name (titleMedium, semibold)
  - Date + days until billing (e.g., "Mar 25, 2026 · In 5 days")
  - Bills due within 3 days show the date text in error color (red)
  - Amount on the right side

## Test Flow

### Step 0: Device Detection & Emulator Setup
Before anything else, verify a device or emulator is available for testing.

1. Run `adb devices` via the Bash tool to list connected devices.
2. **Parse the output** — look for lines after "List of devices attached" that end with `device` (not `offline` or `unauthorized`).
3. **If exactly one device is connected**:
   - Tell the user: "Found device [serial]. Using it for testing."
4. **If multiple devices are connected**:
   - List them all with their serial numbers and ask the user: "I found multiple devices. Which one should I use for testing?"
   - Once the user picks one, set the target by running `adb -s [serial]` for subsequent commands. Tell the ADB MCP tools which device to target.
5. **If no devices are connected**:
   - Explain: "No devices or emulators are currently connected. Let me check what emulators are available."
   - Run `emulator -list-avds` via the Bash tool to list available Android Virtual Devices.
   - **If AVDs are found**: List them and ask: "I found these emulators: [list]. Which one should I start? Or should I use the first one?"
   - **If no AVDs are found**: Tell the user: "No emulators are set up. You'll need to create one in Android Studio (Tools → Device Manager → Create Device). Once it's ready, run this test again."
   - Start the chosen emulator: run `emulator -avd [avd_name] &` via the Bash tool (in background).
   - Explain: "Starting the emulator. This can take 30-60 seconds..."
   - Poll with `adb devices` every few seconds until the device shows as `device` (not `offline`).
   - Run `adb shell getprop sys.boot_completed` and wait until it returns `1` to confirm the emulator is fully booted.
   - Screenshot: `capture_screenshot("upcoming-test", "00_emulator_ready")`
   - Tell the user: "Emulator is ready!"

### Step 1: Setup
1. Create a test folder: `create_test_folder("upcoming-test")`
2. Check app is installed: `list_apps("subscriptiontracker")`
   - If not found, tell the user: "The app isn't installed. Run `./gradlew installDebug` from the project root first." Ask if they need help.
3. Launch: `open_app("com.example.subscriptiontracker")`
4. Capture UI dump, find the "Upcoming" bottom nav tab
5. Tap it to navigate to the Upcoming Bills screen
6. Screenshot: `capture_screenshot("upcoming-test", "01_upcoming_tab")`
7. Ask: "We're on the Upcoming Bills tab. Does the screen look correct?"

### Step 2: Verify Screen State
**If no subscriptions exist** (fresh install):
1. Screenshot: `capture_screenshot("upcoming-test", "02_empty_state")`
2. Explain: "With no subscriptions added, you should see the Total Due card showing $0.00 and 0 upcoming bills, plus the message 'No upcoming bills in the next 30 days'."
3. Ask: "Does the empty state look correct? If you want to test with data, we should add some subscriptions first (use the test-subscriptions skill)."

**If subscriptions exist**:
1. Screenshot: `capture_screenshot("upcoming-test", "02_with_data")`
2. Explain: "The Total Due card should show the sum of all bills due within 30 days. Below it, each subscription with a billing date in the next 30 days should appear as a card."
3. Ask: "Do the amounts and dates look correct for your subscriptions?"

### Step 3: Verify Total Due Summary Card
1. Capture UI dump
2. Look for "Total Due (30 days)" text, the total amount, and the count text
3. Explain: "I can see the summary card showing [total amount] with [N] upcoming bills. The total should equal the sum of all individual bill amounts shown below."
4. Ask: "Does the total match what you'd expect?"

### Step 4: Verify Bill Cards
1. For each visible bill card in the UI dump, verify:
   - Subscription name is present
   - Date and "days until" text is shown
   - Amount is displayed on the right
2. Explain what you found: "I can see [N] bill cards. [List names and amounts]."
3. Screenshot: `capture_screenshot("upcoming-test", "03_bill_cards")`

### Step 5: Verify Urgency Highlighting
If any bills are due within 3 days:
1. Explain: "Bills due within 3 days should have their date text in red (error color) to indicate urgency."
2. Screenshot: `capture_screenshot("upcoming-test", "04_urgency_check")`
3. Ask: "Do any near-due bills have red date text? If all your bills are more than 3 days away, this won't apply."

### Step 6: Test Scrollability
If there are enough bills to scroll:
1. Scroll down: `input_scroll("down")`
2. Screenshot: `capture_screenshot("upcoming-test", "05_scrolled")`
3. Ask: "Are there more bills visible after scrolling?"
4. Scroll back: `input_scroll("up")`

### Step 7: Summary
- ✅ Upcoming Bills screen loads from bottom nav
- ✅ Title "Upcoming Bills" displayed
- ✅ Total Due summary card shows correct total and count
- ✅ Empty state message shown when no bills are due
- ✅ Individual bill cards show name, date, days until, and amount
- ✅ Urgency highlighting (red) for bills due within 3 days
- ✅ Screen scrolls if content overflows

Ask: "Upcoming Bills testing complete! Want me to retest anything or move to another tab?"

## Coordinate Finding
Always capture a UI dump before tapping. Elements have `bounds` like `[left,top][right,bottom]`. Tap center: X = (left+right)/2, Y = (top+bottom)/2. State coordinates before tapping.

## If Something Goes Wrong
- **App crashes**: Screenshot, relaunch, explain
- **Tap misses**: Re-dump UI, recalculate, retry
- **No data showing**: The user may need to add subscriptions first — suggest running the test-subscriptions skill
