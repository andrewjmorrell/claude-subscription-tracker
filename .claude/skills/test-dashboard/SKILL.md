---
name: test-dashboard
description: >
  Test the Dashboard (Home) screen of the Subscription Tracker app using Android Debug Bridge MCP tools.
  Use this skill when the user says things like "test the dashboard", "test the home screen",
  "check the home tab", "verify the dashboard works", "QA the dashboard", or wants to test
  the main overview screen showing monthly/yearly costs and upcoming bills.
---

# Test Dashboard Screen — Subscription Tracker

You are an interactive Android testing guide. Walk the user through testing the Dashboard (Home) tab of the Subscription Tracker app step by step. Explain what you're doing and why at each step, take screenshots to document, and ask the user to confirm results before moving on.

## App Info
- **Package**: `com.example.subscriptiontracker`
- **Dashboard** is the start destination (Home tab, first bottom nav item)

## What the Dashboard Shows
- Title: "Dashboard"
- **Monthly cost** summary card (primaryContainer background)
- **Yearly cost** summary card (primaryContainer background)
- **Active Subscriptions** count card (tappable — navigates to Subs tab)
- **Upcoming Bills (7 days)** section with "See all" link
  - Empty state: "No bills due in the next 7 days"
  - With data: list of subscription names, due dates, and amounts
- **Most Expensive** section with "See all" link
  - Empty state: "No active subscriptions yet"
  - With data: ranked list showing name, billing cycle, and monthly cost

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
   - Screenshot: `capture_screenshot("dashboard-test", "00_emulator_ready")`
   - Tell the user: "Emulator is ready!"

### Step 1: Setup
1. Create a test folder: `create_test_folder("dashboard-test")`
2. Check app is installed: `list_apps("subscriptiontracker")`
   - If not found, tell the user: "The app isn't installed. Run `./gradlew installDebug` from the project root first." Ask if they need help.
3. Launch the app: `open_app("com.example.subscriptiontracker")`
4. Wait a moment, then take a screenshot: `capture_screenshot("dashboard-test", "01_app_launch")`
5. Capture a UI dump to see the screen hierarchy
6. Show the screenshot to the user: "Here's what the app looks like on launch. You should see the Dashboard screen. Does this look right?"

### Step 2: Verify Empty State
If the database is empty (fresh install), the dashboard should show:
- Monthly: $0.00
- Yearly: $0.00
- Active Subscriptions: 0
- "No bills due in the next 7 days"
- "No active subscriptions yet"

1. Take a screenshot: `capture_screenshot("dashboard-test", "02_empty_state")`
2. Ask: "This is the empty state — no subscriptions have been added yet. Do you see $0.00 for Monthly and Yearly, and the empty state messages? If you've already added subscriptions, the totals should reflect those."

### Step 3: Verify Layout Structure
Use the UI dump to confirm the expected elements are present:
- Look for text nodes containing "Dashboard", "Monthly", "Yearly", "Active Subscriptions", "Upcoming Bills", "Most Expensive"
- Tell the user what you found: "I can see all the expected sections in the UI hierarchy: [list them]"

### Step 4: Test the "Active Subscriptions" Card Tap
The Active Subscriptions card is clickable and should navigate to the Subscription List.

1. Use UI dump to find the "Active Subscriptions" card bounds
2. Explain: "The Active Subscriptions card should be tappable. I'll tap it to verify it navigates to the Subs tab."
3. Tap it: `input_tap(x, y)` (center of the card's bounds)
4. Take a screenshot: `capture_screenshot("dashboard-test", "03_after_active_tap")`
5. Ask: "Did we navigate to the Subscription List? You should see a 'Subscriptions' title and a search bar."
6. Navigate back: `input_keyevent("BACK")`
7. Screenshot to confirm we're back on Dashboard: `capture_screenshot("dashboard-test", "04_back_to_dashboard")`

### Step 5: Test "See All" Links
1. Use UI dump to find the "See all" text near "Upcoming Bills"
2. Explain: "The 'See all' next to Upcoming Bills should navigate to the Upcoming tab."
3. Tap it: `input_tap(x, y)`
4. Screenshot: `capture_screenshot("dashboard-test", "05_see_all_upcoming")`
5. Ask: "Did we navigate to the Upcoming Bills screen?"
6. Go back: `input_keyevent("BACK")`

### Step 6: Test Scrollability
If the content is long enough, verify the screen scrolls.

1. Explain: "I'll scroll down to make sure all content is accessible."
2. Scroll: `input_scroll("down")`
3. Screenshot: `capture_screenshot("dashboard-test", "06_scrolled_down")`
4. Ask: "Can you see the 'Most Expensive' section after scrolling? Or was it already visible?"
5. Scroll back: `input_scroll("up")`

### Step 7: Summary
Summarize what was tested:
- ✅ Dashboard loads as start destination
- ✅ Summary cards displayed (Monthly, Yearly, Active count)
- ✅ Upcoming Bills section present
- ✅ Most Expensive section present
- ✅ Active Subscriptions card navigates to Subs tab
- ✅ "See all" link navigates to Upcoming Bills
- ✅ Screen scrolls correctly

Ask: "Dashboard testing complete! Any specific areas you want to retest or investigate further?"

## Coordinate Finding
Always capture a UI dump before tapping. Elements have `bounds` like `[left,top][right,bottom]`. Tap the center:
- X = (left + right) / 2
- Y = (top + bottom) / 2

State coordinates before tapping: "I found the button at bounds [X1,Y1][X2,Y2]. Tapping center at (X, Y)."

## If Something Goes Wrong
- **App crashes**: Screenshot, relaunch with `open_app`, explain what happened
- **Wrong screen**: Use UI dump to figure out where we are, navigate using bottom bar or BACK
- **Tap misses**: Re-dump UI (layout may have shifted), recalculate coordinates
