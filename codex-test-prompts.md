# Android Test Prompts for Codex

These prompts drive interactive testing of the Subscription Tracker Android app via ADB. Each section is a standalone prompt that can be fed to Codex (or any LLM agent with ADB tool access).

---

## Prerequisites

Codex needs access to ADB tools with these capabilities:

| Tool | Description | Parameters |
|------|-------------|------------|
| `create_test_folder(test_name)` | Create output directory for screenshots | `test_name: string` |
| `list_apps(app_name)` | Check if app is installed | `app_name: string` |
| `open_app(package_name)` | Launch app by package | `package_name: string` |
| `capture_screenshot(test_name, step_name)` | Save screenshot to test folder | `test_name: string, step_name: string` |
| `capture_ui_dump()` | Get UI hierarchy XML (element bounds, text, classes) | none |
| `input_tap(x, y)` | Tap at coordinates | `x: number, y: number` |
| `input_text(text)` | Type into focused field | `text: string` |
| `input_keyevent(key)` | Send key event | `key: "BACK" \| "HOME" \| "ENTER" \| "DELETE"` |
| `input_scroll(direction)` | Scroll the screen | `direction: "up" \| "down" \| "left" \| "right"` |

If your ADB MCP server uses different tool names, map them accordingly.

---

## Global Instructions (apply to ALL tests)

### Coordinate Finding
Always capture a UI dump before tapping. Elements have `bounds` like `[left,top][right,bottom]`. Tap the center:
- X = (left + right) / 2
- Y = (top + bottom) / 2

State coordinates before tapping: "Found element at bounds [X1,Y1][X2,Y2]. Tapping center at (X, Y)."

### Error Recovery
- **App crashes**: Screenshot, relaunch with `open_app("com.example.subscriptiontracker")`, explain what happened
- **Wrong screen**: Use UI dump to figure out where you are, navigate using bottom nav or BACK
- **Tap misses**: Re-dump UI (layout may have shifted), recalculate coordinates, retry
- **Can't type text**: Make sure a text field is focused first (tap it, then use `input_text`)

### Known Quirks
- After navigating via a card (not bottom nav), the Home tab may not respond to taps. Use `input_keyevent("BACK")` instead.
- Date picker has a timezone offset — selecting a date may save as the previous day.
- Search appears to be prefix-match, not substring. "nt" won't match "netflix", but "net" will.
- Pressing BACK while a search field is focused can navigate away from the screen entirely instead of just dismissing the keyboard.
- `input_text` may create composing text that requires extra DELETE presses to clear.

### Test Artifacts
After EVERY test run, write two files:

1. **`<test-name>/details.md`** — Full detailed log:
   - Device used and test data present
   - Each step performed with exact coordinates tapped, UI dump observations, and screenshots captured
   - Any issues encountered and how they were resolved
   - A results summary table with PASS/FAIL for each check
   - A table listing all screenshots with descriptions

2. **`<test-name>/test.md`** — Compact rerunnable test script:
   - List exact ADB tool calls to make in order
   - For each check, state what to verify in the UI dump and what constitutes PASS/FAIL
   - End with a summary table template to fill in with results
   - Do NOT include explanatory prose — just the steps and assertions

---

## Device Detection (Step 0 — use at the start of EVERY test)

Before anything else, verify a device or emulator is available for testing.

1. Run `adb devices` to list connected devices.
2. Parse the output — look for lines after "List of devices attached" that end with `device` (not `offline` or `unauthorized`).
3. **If exactly one device is connected**: Tell the user: "Found device [serial]. Using it for testing."
4. **If multiple devices are connected**: List them all and ask: "I found multiple devices. Which one should I use for testing?"
5. **If no devices are connected**:
   - Run `emulator -list-avds` to list available Android Virtual Devices.
   - If AVDs are found: List them and ask which to start.
   - If no AVDs: Tell the user to create one in Android Studio.
   - Start the chosen emulator: `emulator -avd [avd_name] &` (background).
   - Poll with `adb devices` until the device shows as `device`.
   - Run `adb shell getprop sys.boot_completed` and wait until it returns `1`.

---

## Prompt 1: Test Dashboard (Home Tab)

```
You are an interactive Android testing guide. Test the Dashboard (Home) tab of the Subscription Tracker app step by step. Explain what you're doing and why, take screenshots to document, and ask the user to confirm results before moving on.

App Info:
- Package: com.example.subscriptiontracker
- Dashboard is the start destination (Home tab, first bottom nav item)

What the Dashboard Shows:
- Title: "Dashboard"
- Monthly cost summary card (primaryContainer background)
- Yearly cost summary card (primaryContainer background)
- Active Subscriptions count card (tappable — navigates to Subs tab)
- Upcoming Bills (7 days) section with "See all" link
  - Empty state: "No bills due in the next 7 days"
  - With data: list of subscription names, due dates, and amounts
- Most Expensive section with "See all" link
  - Empty state: "No active subscriptions yet"
  - With data: ranked list showing name, billing cycle, and monthly cost

Test Steps:

Step 1: Setup
1. create_test_folder("dashboard-test")
2. list_apps("subscriptiontracker") — if not found, tell user to run ./gradlew installDebug
3. open_app("com.example.subscriptiontracker")
4. capture_screenshot("dashboard-test", "01_app_launch")
5. capture_ui_dump() — confirm Dashboard loaded as start destination
6. Show screenshot: "Here's the Dashboard. Does this look right?"

Step 2: Verify Empty State
If fresh install, dashboard should show:
- Monthly: $0.00, Yearly: $0.00, Active Subscriptions: 0
- "No bills due in the next 7 days"
- "No active subscriptions yet"
1. capture_screenshot("dashboard-test", "02_empty_state")
2. Ask user to confirm values match

Step 3: Verify Layout Structure
Use UI dump to confirm these elements exist:
- "Dashboard", "Monthly", "Yearly", "Active Subscriptions", "Upcoming Bills", "Most Expensive"
- Two "See all" links
- Bottom nav: Home (selected), Subs, Upcoming, Analytics, Settings
Tell user what was found.

Step 4: Test Active Subscriptions Card Tap
1. Find "Active Subscriptions" card bounds in UI dump
2. Tap center of the card
3. capture_screenshot("dashboard-test", "03_after_active_tap")
4. Verify navigation to Subscriptions screen (look for "Subscriptions" title)
5. input_keyevent("BACK") to return
6. capture_screenshot("dashboard-test", "04_back_to_dashboard")

Step 5: Test "See All" Links
1. Find "See all" near "Upcoming Bills" in UI dump
2. Tap it
3. capture_screenshot("dashboard-test", "05_see_all_upcoming")
4. Verify "Upcoming Bills" title is present
5. input_keyevent("BACK") to return

Step 6: Test Scrollability
1. input_scroll("down")
2. capture_screenshot("dashboard-test", "06_scrolled_down")
3. input_scroll("up")

Step 7: Summary
Report PASS/FAIL for each:
- Dashboard loads as start destination
- Summary cards displayed (Monthly, Yearly, Active count)
- Upcoming Bills section present
- Most Expensive section present
- Active Subscriptions card navigates to Subs tab
- "See all" link navigates to Upcoming Bills
- Screen scrolls correctly

Step 8: Write test artifacts (details.md and test.md) into dashboard-test/
```

---

## Prompt 2: Test Subscriptions (Subs Tab)

```
You are an interactive Android testing guide. Test the Subscriptions tab — including the list, add, edit, search, sort, and filter flows. Explain what you're doing and why at each step.

App Info:
- Package: com.example.subscriptiontracker
- Subs tab is the second bottom nav item (label "Subs")

What the Subscription List Shows:
- Title: "Subscriptions"
- Search bar: OutlinedTextField with "Search subscriptions" label
- Sort button (Sort icon) → dropdown: Name, Amount (Low→High), Amount (High→Low), Next Billing Date
- Filter button (FilterList icon) → dropdown: All Categories + all 13 category options
- Active filter chip showing "Filtered: [category]" when a filter is applied
- Empty state: "No subscriptions yet. Tap + to add one!" or "No subscriptions match your filters"
- Subscription cards: Name, category, billing cycle, amount, monthly cost, days until billing, trial badge
- FAB (+ button): Opens Add Subscription screen

What the Add/Edit Screen Shows:
- Top app bar: "Add Subscription" or "Edit Subscription" with back arrow
- Form fields: Name, Amount, Billing Cycle dropdown, Category dropdown, Next Billing Date picker, Trial End Date picker, Notes, Active toggle
- Sticky save button at bottom (always visible)

Test Steps:

Step 1: Setup
1. create_test_folder("subscriptions-test")
2. list_apps("subscriptiontracker") — if not found, tell user to install
3. open_app("com.example.subscriptiontracker")
4. Find "Subs" tab in UI dump, tap it
5. capture_screenshot("subscriptions-test", "01_subs_tab")

Step 2: Verify Empty State
1. capture_screenshot("subscriptions-test", "02_empty_list")
2. Verify "No subscriptions yet. Tap + to add one!" and FAB are present

Step 3: Add First Subscription
1. Find FAB ("Add subscription") in UI dump, tap it
2. capture_screenshot("subscriptions-test", "03_add_form_empty")
3. CRITICAL CHECK: Verify "Save Subscription" button visible at bottom without scrolling (sticky bottom bar)
4. Tap Name field, type "Netflix"
5. Tap Amount field, type "15.99"
6. Set Billing Cycle to "Monthly" (tap dropdown, select)
7. Set Category to "Streaming" (tap dropdown, select)
8. Tap Next Billing Date field to open date picker
9. capture_screenshot("subscriptions-test", "04_date_picker")
10. Select a date and tap "OK"
11. capture_screenshot("subscriptions-test", "05_form_filled")
12. Find and tap "Save Subscription"
13. capture_screenshot("subscriptions-test", "06_after_save")
14. Verify Netflix appears in the list

Step 4: Add Second Subscription
1. Tap FAB again
2. Fill: Name="Spotify", Amount="9.99", Billing Cycle="Monthly", Category="Music"
3. Set a billing date, save
4. capture_screenshot("subscriptions-test", "07_two_subs")
5. Verify both Netflix and Spotify in list

Step 5: Test Edit Flow
1. Find first subscription card in UI dump, tap it
2. capture_screenshot("subscriptions-test", "08_edit_screen")
3. Verify title is "Edit Subscription", button is "Update Subscription", fields pre-filled
4. Clear amount field (DELETE keys), type new value
5. Tap "Update Subscription"
6. capture_screenshot("subscriptions-test", "09_after_edit")
7. Verify updated amount in list

Step 6: Test Search
1. Find search bar in UI dump, tap it
2. Type "net"
3. capture_screenshot("subscriptions-test", "10_search_results")
4. Verify only Netflix shown
5. Clear search (DELETE keys), verify both return

Step 7: Test Sort
1. Find Sort icon button in UI dump, tap it
2. capture_screenshot("subscriptions-test", "11_sort_menu")
3. Tap "Amount (Low → High)"
4. capture_screenshot("subscriptions-test", "12_sorted_by_amount")
5. Verify lower amount appears first

Step 8: Test Filter
1. Find Filter icon button in UI dump, tap it
2. Tap "Streaming"
3. capture_screenshot("subscriptions-test", "13_filtered_streaming")
4. Verify only Streaming subs shown + "Filtered: Streaming" chip
5. Clear filter: tap filter again, select "All Categories"

Step 9: Test Validation
1. Tap FAB to open empty form
2. Immediately tap "Save Subscription" without filling anything
3. capture_screenshot("subscriptions-test", "14_validation_errors")
4. Verify error messages under Name, Amount, and Date fields
5. input_keyevent("BACK")

Step 10: Summary
Report PASS/FAIL for each:
- Empty state displayed correctly
- Add subscription form works (all fields + save)
- Save button is sticky at bottom
- Date picker opens and works
- New subscription appears in list
- Multiple subscriptions display correctly
- Edit flow pre-fills data and updates
- Search filters by name
- Sort reorders the list
- Category filter works with chip indicator
- Form validation shows errors for empty required fields

Step 11: Write test artifacts (details.md and test.md) into subscriptions-test/
```

---

## Prompt 3: Test Upcoming Bills (Upcoming Tab)

```
You are an interactive Android testing guide. Test the Upcoming Bills tab. Explain what you're doing and why at each step.

App Info:
- Package: com.example.subscriptiontracker
- Upcoming tab is the third bottom nav item (label "Upcoming")

What the Upcoming Bills Screen Shows:
- Title: "Upcoming Bills"
- Total Due summary card (primaryContainer background):
  - "Total Due (30 days)" label
  - Total amount in large bold text
  - Count: "N upcoming bill(s)"
- Empty state: "No upcoming bills in the next 30 days"
- Bill cards (surfaceVariant background): Each shows:
  - Subscription name (titleMedium, semibold)
  - Date + days until billing (e.g., "Mar 25, 2026 · In 5 days")
  - Bills due within 3 days show the date text in error color (red)
  - Amount on the right side

Test Steps:

Step 1: Setup
1. create_test_folder("upcoming-test")
2. list_apps("subscriptiontracker") — if not found, tell user to install
3. open_app("com.example.subscriptiontracker")
4. Find "Upcoming" tab in UI dump, tap it
5. capture_screenshot("upcoming-test", "01_upcoming_tab")

Step 2: Verify Screen State
If no subscriptions: verify $0.00 total, 0 bills, "No upcoming bills in the next 30 days"
If subscriptions exist: verify total and individual bill cards

Step 3: Verify Total Due Summary Card
1. capture_ui_dump()
2. Find "Total Due (30 days)", total amount, count text
3. Verify total equals sum of individual bill amounts

Step 4: Verify Bill Cards
For each visible bill card, verify:
- Subscription name present
- Date and "days until" text shown
- Amount displayed on the right
capture_screenshot("upcoming-test", "03_bill_cards")

Step 5: Verify Urgency Highlighting
Bills due within 3 days should have red date text.
capture_screenshot("upcoming-test", "04_urgency_check")

Step 6: Test Scrollability
1. input_scroll("down")
2. capture_screenshot("upcoming-test", "05_scrolled")
3. input_scroll("up")

Step 7: Summary
Report PASS/FAIL for each:
- Upcoming Bills screen loads from bottom nav
- Title "Upcoming Bills" displayed
- Total Due summary card shows correct total and count
- Empty state message shown when no bills are due
- Individual bill cards show name, date, days until, and amount
- Urgency highlighting (red) for bills due within 3 days
- Screen scrolls if content overflows

Step 8: Write test artifacts (details.md and test.md) into upcoming-test/
```

---

## Prompt 4: Test Analytics (Analytics Tab)

```
You are an interactive Android testing guide. Test the Analytics tab. Explain what you're doing and why at each step.

App Info:
- Package: com.example.subscriptiontracker
- Analytics tab is the fourth bottom nav item (label "Analytics")

What the Analytics Screen Shows:
- Title: "Analytics"
- Cost summary cards (primaryContainer):
  - Monthly total and Yearly total (side by side)
  - Daily Average (full width below)
- "Spending by Category" section:
  - Empty state: "No active subscriptions to analyze"
  - With data: Card with one row per category:
    - Category name (left) and monthly cost (right, e.g., "$15.99/mo")
    - Progress bar showing percentage of total spending
    - Percentage label (e.g., "62%")
    - Subscription count (e.g., "2 subscriptions")
    - Horizontal dividers between categories
- "Summary" section: Card with rows:
  - Active Subscriptions: count
  - Categories Used: count
  - Avg per Subscription: average monthly cost or "N/A"
- The entire screen scrolls vertically

Test Steps:

Step 1: Setup
1. create_test_folder("analytics-test")
2. list_apps("subscriptiontracker") — if not found, tell user to install
3. open_app("com.example.subscriptiontracker")
4. Find "Analytics" tab in UI dump, tap it
5. capture_screenshot("analytics-test", "01_analytics_tab")

Step 2: Verify Cost Summary Cards
1. capture_ui_dump()
2. Find Monthly, Yearly, and Daily Average values
3. Verify daily average ≈ yearly / 365
4. capture_screenshot("analytics-test", "02_cost_cards")

Step 3: Verify Empty vs Populated State
If no subscriptions: verify $0.00 totals and "No active subscriptions to analyze"
If subscriptions exist: continue to Step 4

Step 4: Verify Category Breakdown
1. input_scroll("down") if needed
2. capture_screenshot("analytics-test", "04_category_breakdown")
3. Find category names, amounts, percentages
4. Verify percentages add up to ~100%

Step 5: Verify Progress Bars
Each category has a progress bar proportional to its percentage.
capture_screenshot("analytics-test", "05_progress_bars")

Step 6: Verify Summary Section
1. input_scroll("down") to Summary section
2. capture_screenshot("analytics-test", "06_summary")
3. Find "Active Subscriptions", "Categories Used", "Avg per Subscription"
4. Verify avg = monthly total / active count

Step 7: Test Full Scroll
1. Scroll to top, then scroll all the way down
2. capture_screenshot("analytics-test", "07_full_scroll")
3. Verify all content accessible

Step 8: Summary
Report PASS/FAIL for each:
- Analytics screen loads from bottom nav
- Title "Analytics" displayed
- Monthly, Yearly, and Daily Average cost cards correct
- Category breakdown shows categories with amounts and progress bars
- Percentages add up correctly
- Summary section shows subscription count, categories used, and average
- Full screen scrolls vertically

Step 9: Write test artifacts (details.md and test.md) into analytics-test/
```

---

## Prompt 5: Test Settings (Settings Tab)

```
You are an interactive Android testing guide. Test the Settings tab. Explain what you're doing and why at each step. This screen is especially important because it changes app-wide behavior (theme, currency symbol).

App Info:
- Package: com.example.subscriptiontracker
- Settings tab is the fifth (last) bottom nav item (label "Settings")

What the Settings Screen Shows:
- Title: "Settings"
- Appearance section:
  - Dark Mode toggle with "Use dark color theme" subtitle
- Currency section:
  - Currency Symbol dropdown (options: $, €, £, ¥, ₹, ₩, A$, C$, CHF, R$)
- Notifications section:
  - Billing Reminders toggle with "Get notified before bills are due" subtitle
  - When reminders are ON: "Remind me" dropdown (Same day, 1 day before, 3 days before)
- About section:
  - "Subscription Tracker" and "Version 1.0"
- The entire screen scrolls vertically

Test Steps:

Step 1: Setup
1. create_test_folder("settings-test")
2. list_apps("subscriptiontracker") — if not found, tell user to install
3. open_app("com.example.subscriptiontracker")
4. Find "Settings" tab in UI dump, tap it
5. capture_screenshot("settings-test", "01_settings_tab")

Step 2: Verify Initial State
1. capture_ui_dump()
2. Find Dark Mode switch state, currency symbol, reminders switch state
3. Report current values
4. capture_screenshot("settings-test", "02_initial_state")

Step 3: Test Dark Mode Toggle
1. Find Dark Mode switch in UI dump, tap it
2. capture_screenshot("settings-test", "03_dark_mode_on")
3. Verify entire theme changed (dark backgrounds, light text)
4. Re-dump UI (layout shifts with theme), find switch again, tap it back
5. capture_screenshot("settings-test", "04_dark_mode_off")
6. Verify theme reverted

Step 4: Test Currency Change
1. Find Currency Symbol dropdown in UI dump, tap it
2. capture_screenshot("settings-test", "05_currency_dropdown")
3. Find "€" option, tap it
4. capture_screenshot("settings-test", "06_currency_changed")
5. Navigate to Home tab, verify € symbol on Dashboard
6. capture_screenshot("settings-test", "07_dashboard_euro")
7. Navigate back to Settings, change currency back to $

Step 5: Test Reminders Toggle
1. Find Billing Reminders switch in UI dump
2. If ON: verify "Remind me" dropdown is visible
3. Toggle OFF: tap switch
4. capture_screenshot("settings-test", "09_reminders_off")
5. Verify "Remind me" dropdown disappeared
6. Toggle back ON: tap switch
7. capture_screenshot("settings-test", "10_reminders_back_on")
8. Verify "Remind me" dropdown reappeared

Step 6: Test Reminder Timing Dropdown
1. Find "Remind me" dropdown in UI dump, tap it
2. capture_screenshot("settings-test", "11_timing_dropdown")
3. Verify 3 options: Same day, 1 day before, 3 days before
4. Tap "3 days before"
5. capture_screenshot("settings-test", "12_timing_changed")
6. Verify selection saved

Step 7: Verify About Section
1. input_scroll("down") if needed
2. capture_screenshot("settings-test", "13_about_section")
3. Verify "Subscription Tracker" and "Version 1.0" present

Step 8: Summary
Report PASS/FAIL for each:
- Settings screen loads from bottom nav
- Dark Mode toggle switches theme immediately (and back)
- Currency dropdown shows all options and selection propagates to Dashboard
- Reminders toggle shows/hides the timing dropdown
- Reminder timing dropdown works with all 3 options
- About section shows app name and version
- Screen scrolls if needed

Step 9: Write test artifacts (details.md and test.md) into settings-test/
```

---

## Running Order

For best results, run in this order (each test may create data needed by the next):

1. **test-subscriptions** — Creates Netflix + Spotify test data
2. **test-dashboard** — Verifies totals and navigation with data present
3. **test-upcoming** — Verifies billing dates and urgency highlighting
4. **test-analytics** — Verifies category breakdown and percentages
5. **test-settings** — Tests app-wide settings (dark mode, currency, reminders)
