---
name: test-analytics
description: >
  Test the Analytics screen of the Subscription Tracker app using Android Debug Bridge MCP tools.
  Use this skill when the user says things like "test analytics", "test the analytics tab",
  "check spending breakdown", "verify analytics works", "test the category breakdown",
  or wants to test the screen showing spending analysis and category percentages.
---

# Test Analytics Screen — Subscription Tracker

You are an interactive Android testing guide. Walk the user through testing the Analytics tab. Explain what you're doing and why at each step.

## App Info
- **Package**: `com.example.subscriptiontracker`
- **Analytics tab** is the fourth bottom nav item (label "Analytics")

## What the Analytics Screen Shows
- Title: "Analytics"
- **Cost summary cards** (primaryContainer):
  - Monthly total and Yearly total (side by side)
  - Daily Average (full width below)
- **"Spending by Category" section**:
  - Empty state: "No active subscriptions to analyze"
  - With data: Card containing one row per category with:
    - Category name (left) and monthly cost (right, e.g., "$15.99/mo")
    - Progress bar showing percentage of total spending
    - Percentage label (e.g., "62%")
    - Subscription count (e.g., "2 subscriptions")
    - Horizontal dividers between categories
- **"Summary" section**: Card with rows:
  - Active Subscriptions: count
  - Categories Used: count
  - Avg per Subscription: average monthly cost or "N/A"
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
   - Screenshot: `capture_screenshot("analytics-test", "00_emulator_ready")`
   - Tell the user: "Emulator is ready!"

### Step 1: Setup
1. Create a test folder: `create_test_folder("analytics-test")`
2. Check app is installed: `list_apps("subscriptiontracker")`
   - If not found, tell the user: "The app isn't installed. Run `./gradlew installDebug` from the project root first." Ask if they need help.
3. Launch: `open_app("com.example.subscriptiontracker")`
4. Capture UI dump, find the "Analytics" bottom nav tab
5. Tap it
6. Screenshot: `capture_screenshot("analytics-test", "01_analytics_tab")`
7. Ask: "We're on the Analytics tab. Does the screen look correct?"

### Step 2: Verify Cost Summary Cards
1. Capture UI dump
2. Look for the Monthly, Yearly, and Daily Average cards
3. Explain: "I can see three cost summary cards: Monthly showing [amount], Yearly showing [amount], and Daily Average showing [amount]. The daily average should roughly equal the yearly total divided by 365."
4. Screenshot: `capture_screenshot("analytics-test", "02_cost_cards")`
5. Ask: "Do the Monthly, Yearly, and Daily Average amounts look correct for your subscriptions?"

### Step 3: Verify Empty vs Populated State
**If no subscriptions**:
1. Explain: "With no active subscriptions, you should see $0.00 for all totals and the message 'No active subscriptions to analyze'."
2. Screenshot: `capture_screenshot("analytics-test", "03_empty_state")`
3. Ask: "Does the empty state look right? You'll need to add subscriptions to see the category breakdown."

**If subscriptions exist**:
Continue to Step 4.

### Step 4: Verify Category Breakdown
1. Scroll down if needed: `input_scroll("down")`
2. Screenshot: `capture_screenshot("analytics-test", "04_category_breakdown")`
3. Capture UI dump
4. Look for category names, amounts, and percentage text
5. Explain: "I can see the category breakdown: [list categories with amounts and percentages]. The percentages should add up to 100% and the progress bars should visually represent each category's share of total spending."
6. Ask: "Does the breakdown match what you'd expect? The categories should be sorted by highest monthly cost first."

### Step 5: Verify Progress Bars
1. Explain: "Each category has a progress bar showing its percentage of total spending. The top category should have the longest bar. Visually, do the bars proportionally match the percentages shown?"
2. Screenshot: `capture_screenshot("analytics-test", "05_progress_bars")`
3. Ask: "Do the progress bars look proportional to the percentages?"

### Step 6: Verify Summary Section
1. Scroll down to the Summary section: `input_scroll("down")`
2. Screenshot: `capture_screenshot("analytics-test", "06_summary")`
3. Capture UI dump
4. Look for "Active Subscriptions", "Categories Used", "Avg per Subscription"
5. Explain: "The Summary section shows [N] active subscriptions across [N] categories, with an average of [amount]/mo per subscription."
6. Ask: "Does the summary data match? The average should equal the monthly total divided by the number of active subscriptions."

### Step 7: Test Full Scroll
1. Scroll back to top: `input_scroll("up")` (repeat if needed)
2. Then scroll all the way down: `input_scroll("down")` (repeat if needed)
3. Screenshot at the bottom: `capture_screenshot("analytics-test", "07_full_scroll")`
4. Ask: "Could you see all the content by scrolling? Everything from the cost cards at the top to the Summary at the bottom should be accessible."

### Step 8: Summary
- ✅ Analytics screen loads from bottom nav
- ✅ Title "Analytics" displayed
- ✅ Monthly, Yearly, and Daily Average cost cards correct
- ✅ Category breakdown shows categories with amounts and progress bars
- ✅ Percentages add up correctly
- ✅ Summary section shows subscription count, categories used, and average
- ✅ Full screen scrolls vertically

### Step 9: Write Test Artifacts
After completing the test, write two files into the test folder:

1. **`analytics-test/details.md`** — A detailed log of everything that happened during this test run:
   - Device used and test data present
   - Each step performed with exact coordinates tapped, UI dump observations, and screenshots captured
   - Any issues encountered and how they were resolved
   - A results summary table with PASS/FAIL for each check
   - A table listing all screenshots with descriptions

2. **`analytics-test/test.md`** — A compact, rerunnable test script optimized for minimal token usage:
   - List exact ADB MCP tool calls to make in order (create folder, list apps, open app, capture dumps, tap, screenshot)
   - For each check, state what to verify in the UI dump and what constitutes PASS/FAIL
   - End with a summary table template to fill in with results
   - Do NOT include explanatory prose — just the steps and assertions

Ask: "Analytics testing complete! Want me to retest anything or check another tab?"

## Coordinate Finding
Always capture a UI dump before tapping. Elements have `bounds` like `[left,top][right,bottom]`. Tap center: X = (left+right)/2, Y = (top+bottom)/2. State coordinates before tapping.

## If Something Goes Wrong
- **App crashes**: Screenshot, relaunch, explain
- **No data**: User needs to add subscriptions first — suggest the test-subscriptions skill
- **Tap misses**: Re-dump UI, recalculate, retry
