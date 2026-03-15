---
name: test-settings
description: >
  Test the Settings screen of the Subscription Tracker app using Android Debug Bridge MCP tools.
  Use this skill when the user says things like "test settings", "test the settings tab",
  "test dark mode", "test currency change", "verify settings works", "test reminders toggle",
  or wants to test the preferences screen with dark mode, currency, and notification settings.
---

# Test Settings Screen — Subscription Tracker

You are an interactive Android testing guide. Walk the user through testing the Settings tab. Explain what you're doing and why at each step. This screen is especially important because it changes app-wide behavior (theme, currency symbol).

## App Info
- **Package**: `com.example.subscriptiontracker`
- **Settings tab** is the fifth (last) bottom nav item (label "Settings")

## What the Settings Screen Shows
- Title: "Settings"
- **Appearance** section:
  - Dark Mode toggle with "Use dark color theme" subtitle
- **Currency** section:
  - Currency Symbol dropdown (options: $, €, £, ¥, ₹, ₩, A$, C$, CHF, R$)
- **Notifications** section:
  - Billing Reminders toggle with "Get notified before bills are due" subtitle
  - When reminders are ON: "Remind me" dropdown (Same day, 1 day before, 3 days before)
- **About** section:
  - "Subscription Tracker" and "Version 1.0"
- The entire screen scrolls vertically

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
   - Screenshot: `capture_screenshot("settings-test", "00_emulator_ready")`
   - Tell the user: "Emulator is ready!"

### Step 1: Setup
1. Create a test folder: `create_test_folder("settings-test")`
2. Check app is installed: `list_apps("subscriptiontracker")`
   - If not found, tell the user: "The app isn't installed. Run `./gradlew installDebug` from the project root first." Ask if they need help.
3. Launch: `open_app("com.example.subscriptiontracker")`
4. Capture UI dump, find the "Settings" bottom nav tab
5. Tap it
6. Screenshot: `capture_screenshot("settings-test", "01_settings_tab")`
7. Ask: "We're on the Settings tab. You should see sections for Appearance, Currency, Notifications, and About. Does it look correct?"

### Step 2: Verify Initial State
1. Capture UI dump
2. Look for the current values: Dark Mode switch state, currency symbol, reminders switch state
3. Explain: "Current settings: Dark Mode is [ON/OFF], Currency is [symbol], Reminders are [ON/OFF]."
4. Screenshot: `capture_screenshot("settings-test", "02_initial_state")`
5. Ask: "These are the default settings. Does everything look right?"

### Step 3: Test Dark Mode Toggle
This is a highly visible change — the entire app theme switches.

1. Capture UI dump, find the Dark Mode switch
2. Explain: "I'll toggle Dark Mode. The app's entire color scheme should change immediately."
3. Tap the switch: `input_tap(x, y)`
4. Wait a moment for the theme to apply
5. Screenshot: `capture_screenshot("settings-test", "03_dark_mode_on")`
6. Ask: "The app should now be in dark mode — dark backgrounds with light text. Does the theme look correct? Check that the bottom nav, cards, and text all switched."

7. Explain: "Now I'll toggle it back off."
8. Capture UI dump (layout may have shifted due to theme change), find the switch again
9. Tap it: `input_tap(x, y)`
10. Screenshot: `capture_screenshot("settings-test", "04_dark_mode_off")`
11. Ask: "We're back to light mode. Did the theme revert correctly?"

### Step 4: Test Currency Change
Changing the currency affects every screen that shows money (Dashboard, Subs, Upcoming, Analytics).

1. Capture UI dump, find the Currency Symbol dropdown
2. Explain: "I'll change the currency from $ to € to verify it propagates across the app."
3. Tap the dropdown: `input_tap(x, y)`
4. Screenshot: `capture_screenshot("settings-test", "05_currency_dropdown")`
5. Ask: "The currency dropdown should show options like $, €, £, ¥, etc. Do you see the list?"

6. Capture UI dump, find the "€" option
7. Tap it: `input_tap(x, y)`
8. Screenshot: `capture_screenshot("settings-test", "06_currency_changed")`
9. Explain: "Currency is now set to €. Let me navigate to the Dashboard to verify it took effect."

10. Capture UI dump, find the "Home" bottom nav tab
11. Tap it: `input_tap(x, y)`
12. Screenshot: `capture_screenshot("settings-test", "07_dashboard_euro")`
13. Ask: "On the Dashboard, the Monthly and Yearly totals should now show € instead of $. Do you see the euro symbol?"

14. Navigate back to Settings tab
15. Change currency back to $ (or leave as-is if user prefers)

### Step 5: Test Reminders Toggle
1. Capture UI dump on the Settings screen, find the Billing Reminders switch
2. Note current state. If reminders are ON:
   - Explain: "Reminders are currently enabled. You should see a 'Remind me' dropdown below the toggle."
   - Screenshot: `capture_screenshot("settings-test", "08_reminders_on")`
   - Ask: "Do you see the reminder timing dropdown (Same day, 1 day before, 3 days before)?"

3. Toggle reminders OFF:
   - Tap the switch: `input_tap(x, y)`
   - Screenshot: `capture_screenshot("settings-test", "09_reminders_off")`
   - Explain: "With reminders turned off, the 'Remind me' dropdown should disappear."
   - Ask: "Did the timing dropdown hide when reminders were turned off?"

4. Toggle reminders back ON:
   - Tap the switch again
   - Screenshot: `capture_screenshot("settings-test", "10_reminders_back_on")`
   - Ask: "The 'Remind me' dropdown should reappear. Is it back?"

### Step 6: Test Reminder Timing Dropdown
1. Capture UI dump, find the "Remind me" dropdown
2. Tap it: `input_tap(x, y)`
3. Screenshot: `capture_screenshot("settings-test", "11_timing_dropdown")`
4. Ask: "You should see three options: Same day, 1 day before, 3 days before. Do you see all three?"
5. Tap "3 days before": `input_tap(x, y)`
6. Screenshot: `capture_screenshot("settings-test", "12_timing_changed")`
7. Ask: "The dropdown should now show '3 days before'. Correct?"

### Step 7: Verify About Section
1. Scroll down if needed: `input_scroll("down")`
2. Screenshot: `capture_screenshot("settings-test", "13_about_section")`
3. Look for "Subscription Tracker" and "Version 1.0" in UI dump
4. Ask: "The About section should show 'Subscription Tracker' and 'Version 1.0'. Do you see it?"

### Step 8: Summary
- ✅ Settings screen loads from bottom nav
- ✅ Dark Mode toggle switches theme immediately (and back)
- ✅ Currency dropdown shows all options and selection propagates to Dashboard
- ✅ Reminders toggle shows/hides the timing dropdown
- ✅ Reminder timing dropdown works with all 3 options
- ✅ About section shows app name and version
- ✅ Screen scrolls if needed

Ask: "Settings testing complete! This was the last of the 5 tabs. Want me to retest anything?"

## Coordinate Finding
Always capture a UI dump before tapping. Elements have `bounds` like `[left,top][right,bottom]`. Tap center: X = (left+right)/2, Y = (top+bottom)/2. State coordinates before tapping.

## If Something Goes Wrong
- **App crashes on theme change**: This would be a significant bug — screenshot, document, relaunch
- **Currency didn't propagate**: Navigate away and back — it should be reactive via Flow/StateFlow
- **Tap misses on switch**: Switches can be small. Re-dump, target the switch specifically (not the text label)
- **Dropdown doesn't open**: May need to tap the trailing icon specifically. Re-dump and find the exact bounds
