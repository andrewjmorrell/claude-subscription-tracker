---
name: test-subscriptions
description: >
  Test the Subscriptions List screen and Add/Edit flows of the Subscription Tracker app using Android Debug Bridge MCP tools.
  Use this skill when the user says things like "test subscriptions", "test the subs tab", "test adding a subscription",
  "test the subscription list", "test edit subscription", "test swipe to delete", "test search and filter",
  or wants to verify the CRUD operations for subscriptions.
---

# Test Subscriptions Screen — Subscription Tracker

You are an interactive Android testing guide. Walk the user through testing the Subscriptions tab — including the list, add, edit, delete, search, sort, and filter flows. Explain what you're doing and why at each step.

## App Info
- **Package**: `com.example.subscriptiontracker`
- **Subs tab** is the second bottom nav item (label "Subs")

## What the Subscription List Shows
- Title: "Subscriptions"
- **Search bar**: OutlinedTextField with "Search subscriptions" label
- **Sort button** (Sort icon) → dropdown: Name, Amount (Low→High), Amount (High→Low), Next Billing Date
- **Filter button** (FilterList icon) → dropdown: All Categories + all 13 category options
- Active filter chip showing "Filtered: [category]" when a filter is applied
- **Empty state**: "No subscriptions yet. Tap + to add one!" or "No subscriptions match your filters"
- **Subscription cards**: Name, category, billing cycle, amount, monthly cost, days until billing, trial badge
- **Swipe-to-delete**: Right-to-left swipe shows red background with delete icon, then Snackbar with "Undo"
- **FAB** (+ button): Opens Add Subscription screen

## What the Add/Edit Screen Shows
- Top app bar: "Add Subscription" or "Edit Subscription" with back arrow
- Form fields: Name, Amount, Billing Cycle dropdown, Category dropdown, Next Billing Date picker, Trial End Date picker, Notes, Active toggle
- **Sticky save button** at bottom (always visible — this was a past bug fix)

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
   - Screenshot: `capture_screenshot("subscriptions-test", "00_emulator_ready")`
   - Tell the user: "Emulator is ready!"

### Step 1: Setup
1. Create a test folder: `create_test_folder("subscriptions-test")`
2. Check app is installed: `list_apps("subscriptiontracker")`
   - If not found, tell the user: "The app isn't installed. Run `./gradlew installDebug` from the project root first." Ask if they want help.
3. Launch: `open_app("com.example.subscriptiontracker")`
4. Wait, then capture UI dump to find the "Subs" bottom nav tab
5. Tap the "Subs" tab to navigate to the Subscription List
6. Screenshot: `capture_screenshot("subscriptions-test", "01_subs_tab")`
7. Ask: "We're on the Subscriptions tab. Does the screen look correct?"

### Step 2: Verify Empty State
1. Screenshot: `capture_screenshot("subscriptions-test", "02_empty_list")`
2. Ask: "You should see 'No subscriptions yet. Tap + to add one!' and a floating + button in the bottom-right. Do you see both?"

### Step 3: Add First Subscription
This tests the core add flow — the most important write operation in the app.

1. Capture UI dump, find the FAB (content description "Add subscription")
2. Explain: "I'll tap the + button to open the Add Subscription form."
3. Tap FAB: `input_tap(x, y)`
4. Screenshot: `capture_screenshot("subscriptions-test", "03_add_form_empty")`
5. **Critical check**: Ask: "Can you see the 'Save Subscription' button at the bottom without scrolling? This button must always be visible (it's a sticky bottom bar)."

6. Capture UI dump, find the "Subscription Name" text field
7. Tap the name field: `input_tap(x, y)`
8. Type: `input_text("Netflix")`
9. Tap the amount field, type: `input_text("15.99")`

10. Explain: "Now I'll set the Billing Cycle to Monthly."
11. Capture UI dump, find "Billing Cycle" dropdown
12. Tap it, then capture UI dump to find "Monthly" option, tap it

13. Explain: "Setting category to Streaming."
14. Find and tap the "Category" dropdown, find and tap "Streaming"

15. Explain: "Now I'll set the next billing date. The date field is a clickable box that opens a date picker dialog."
16. Capture UI dump, find the "Next Billing Date" field
17. Tap it to open the date picker dialog
18. Screenshot: `capture_screenshot("subscriptions-test", "04_date_picker")`
19. Ask: "The date picker dialog should be open. Do you see it?"
20. Find and tap "OK" to confirm (or select a specific date first)

21. Screenshot the filled form: `capture_screenshot("subscriptions-test", "05_form_filled")`
22. Ask: "The form should show Netflix, $15.99, Monthly, Streaming, and a billing date. Does everything look correct?"

23. Capture UI dump, find "Save Subscription" button
24. Tap save: `input_tap(x, y)`
25. Screenshot: `capture_screenshot("subscriptions-test", "06_after_save")`
26. Ask: "We should be back on the Subscription List with Netflix showing in a card. Do you see it?"

### Step 4: Add Second Subscription
Repeat the add flow with different data to verify multiple items work:
1. Tap FAB again
2. Fill: Name="Spotify", Amount="9.99", Billing Cycle="Monthly", Category="Music"
3. Set a next billing date
4. Save
5. Screenshot: `capture_screenshot("subscriptions-test", "07_two_subs")`
6. Ask: "Both Netflix and Spotify should appear in the list now. Do you see both cards?"

### Step 5: Test Edit Flow
1. Capture UI dump, find the first subscription card
2. Explain: "I'll tap on a subscription card to open the edit screen."
3. Tap the card: `input_tap(x, y)`
4. Screenshot: `capture_screenshot("subscriptions-test", "08_edit_screen")`
5. Ask: "The edit form should show the subscription's data pre-filled, with 'Edit Subscription' in the title bar and an 'Update Subscription' button. Do you see all of this?"
6. Find the amount field, clear it and type a new value
7. Tap "Update Subscription"
8. Screenshot: `capture_screenshot("subscriptions-test", "09_after_edit")`
9. Ask: "The updated amount should be reflected in the list. Does it look correct?"

### Step 6: Test Search
1. Capture UI dump, find the search bar
2. Tap the search field: `input_tap(x, y)`
3. Type: `input_text("net")`
4. Screenshot: `capture_screenshot("subscriptions-test", "10_search_results")`
5. Ask: "Only Netflix should appear in the filtered list. Do you see just the one result?"
6. Clear search: press DELETE multiple times or find and clear the field
7. Verify both subscriptions return

### Step 7: Test Sort
1. Capture UI dump, find the Sort icon button
2. Tap it to open the dropdown
3. Screenshot: `capture_screenshot("subscriptions-test", "11_sort_menu")`
4. Tap "Amount (High → Low)"
5. Screenshot: `capture_screenshot("subscriptions-test", "12_sorted_by_amount")`
6. Ask: "The subscription with the higher amount should appear first. Is the order correct?"

### Step 8: Test Filter
1. Capture UI dump, find the Filter icon button
2. Tap it to open the category dropdown
3. Tap "Streaming"
4. Screenshot: `capture_screenshot("subscriptions-test", "13_filtered_streaming")`
5. Ask: "Only Streaming subscriptions should show, with a 'Filtered: Streaming' chip visible. Correct?"
6. Tap filter again, select "All Categories" to clear

### Step 9: Test Validation
1. Tap FAB to add a new subscription
2. Immediately tap "Save Subscription" without filling anything
3. Screenshot: `capture_screenshot("subscriptions-test", "14_validation_errors")`
4. Ask: "Validation errors should appear under the Name, Amount, and Date fields. Do you see error messages?"
5. Go back: `input_keyevent("BACK")`

### Step 10: Summary
- ✅ Empty state displayed correctly
- ✅ Add subscription form works (all fields + save)
- ✅ Save button is sticky at bottom (always visible)
- ✅ Date picker opens and works
- ✅ New subscription appears in list after save
- ✅ Multiple subscriptions display correctly
- ✅ Edit flow pre-fills data and updates
- ✅ Search filters by name
- ✅ Sort reorders the list
- ✅ Category filter works with chip indicator
- ✅ Form validation shows errors for empty required fields

Ask: "Subscriptions testing complete! Want me to test anything else, or investigate any issues?"

## Coordinate Finding
Always capture a UI dump before tapping. Elements have `bounds` like `[left,top][right,bottom]`. Tap the center:
- X = (left + right) / 2
- Y = (top + bottom) / 2

State coordinates before tapping: "I found the element at bounds [X1,Y1][X2,Y2]. Tapping center at (X, Y)."

## If Something Goes Wrong
- **App crashes**: Screenshot, relaunch, explain what happened
- **Tap misses**: Re-dump UI, recalculate, retry
- **Can't type text**: Make sure a text field is focused first (tap it, then use input_text)
- **Date picker tricky**: If the date picker is hard to interact with via ADB, ask the user to manually pick a date and then screenshot the result
