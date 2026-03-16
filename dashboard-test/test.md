# Dashboard Test Script

Rerunnable test for the Dashboard screen. Execute each step in order using ADB MCP tools. Report PASS/FAIL for each check.

## Setup

1. `create_test_folder("dashboard-test")`
2. `list_apps("subscriptiontracker")` — if missing, stop and tell user to run `./gradlew installDebug`
3. `open_app("com.example.subscriptiontracker")`
4. `capture_screenshot("dashboard-test", "01_launch")`
5. `capture_ui_dump()` — save element positions for later steps

## Check 1: Layout

Verify these texts exist in the UI dump:
- "Dashboard"
- "Monthly" with a dollar amount
- "Yearly" with a dollar amount
- "Active Subscriptions" with a number
- "Upcoming Bills (7 days)"
- "Most Expensive"
- Two "See all" links (one per section)
- Bottom nav: "Home" (selected), "Subs", "Upcoming", "Analytics", "Settings"

Report which elements are present/missing. Verify yearly = monthly x 12.

## Check 2: Active Subscriptions Card Navigation

1. Find the clickable element containing "Active Subscriptions" — get its bounds
2. Tap center of that element
3. `capture_screenshot("dashboard-test", "02_active_sub_tap")`
4. `capture_ui_dump()` — verify "Subscriptions" title is present (confirms navigation to Subs tab)
5. Report PASS if Subscriptions screen loaded, FAIL otherwise
6. `input_keyevent("BACK")` to return to Dashboard
7. `capture_ui_dump()` — verify "Dashboard" title is present
8. `capture_screenshot("dashboard-test", "03_back_from_subs")`

## Check 3: "See All" Upcoming Bills Navigation

1. Find the clickable "See all" element near "Upcoming Bills (7 days)" — get its bounds
2. Tap center of that element
3. `capture_screenshot("dashboard-test", "04_see_all_upcoming")`
4. `capture_ui_dump()` — verify "Upcoming Bills" title is present
5. If bills exist, verify each bill card has: name, date text, amount
6. Check for urgency highlighting: any bill with "Due today", "In 1 day", or "In 2 days" should have its date in error/red color
7. Report PASS if Upcoming Bills screen loaded, FAIL otherwise
8. `input_keyevent("BACK")` to return to Dashboard

## Check 4: Scrollability

1. `input_scroll("down")`
2. `capture_screenshot("dashboard-test", "05_scrolled")`
3. `input_scroll("up")`
4. Report PASS (scroll command accepted; visual change depends on content length)

## Results

Print a summary table:

| Check | Status |
|-------|--------|
| Layout elements present | ? |
| Monthly/Yearly math consistent | ? |
| Active Subscriptions card navigates to Subs | ? |
| BACK returns to Dashboard | ? |
| "See all" navigates to Upcoming Bills | ? |
| Urgency highlighting on near-due bills | ? |
| Scroll works | ? |

List any failures with details. State total PASS/FAIL count.
