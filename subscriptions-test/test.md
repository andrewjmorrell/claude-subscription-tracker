# Subscriptions Test Script

Rerunnable test for the Subscriptions tab. Execute each step in order using ADB MCP tools. Report PASS/FAIL for each check.

## Setup

1. `create_test_folder("subscriptions-test")`
2. `list_apps("subscriptiontracker")` — if missing, stop
3. `open_app("com.example.subscriptiontracker")`
4. `capture_ui_dump()` — find "Subs" tab, tap its center
5. `capture_screenshot("subscriptions-test", "01_subs_tab")`
6. `capture_ui_dump()` — verify "Subscriptions" title present

## Check 1: List Layout

Verify in UI dump:
- "Subscriptions" title
- Search bar ("Search subscriptions")
- Sort and Filter buttons (content-desc "Sort" and "Filter")
- FAB (content-desc "Add subscription")
- Bottom nav with "Subs" selected
- If subs exist: subscription cards with name, category, cycle, amount

## Check 2: Edit Flow

1. `capture_ui_dump()` — find first subscription card, note its bounds
2. Tap card center
3. `capture_screenshot("subscriptions-test", "02_edit_screen")`
4. `capture_ui_dump()` — verify:
   - Title = "Edit Subscription"
   - Button = "Update Subscription"
   - Name, Amount, Billing Cycle, Category, Date fields pre-filled
   - "Update Subscription" button visible (sticky bottom)
5. Tap Amount field, DELETE to clear, type new value
6. Tap "Update Subscription"
7. `capture_ui_dump()` — verify updated amount in list
8. `capture_screenshot("subscriptions-test", "03_after_edit")`

## Check 3: Search

1. `capture_ui_dump()` — find search field
2. Tap search field
3. `input_text("net")`
4. `capture_ui_dump()` — verify only "netflix" card visible, Spotify hidden
5. `capture_screenshot("subscriptions-test", "04_search")`
6. DELETE x3 to clear → verify both subs return

## Check 4: Sort

1. Tap Sort button (content-desc "Sort")
2. `capture_ui_dump()` — verify 4 options: Name, Amount (Low → High), Amount (High → Low), Next Billing Date
3. `capture_screenshot("subscriptions-test", "05_sort_menu")`
4. Tap "Amount (Low → High)"
5. `capture_ui_dump()` — verify lower amount card appears first
6. `capture_screenshot("subscriptions-test", "06_sorted")`

## Check 5: Filter

1. Tap Filter button (content-desc "Filter")
2. `capture_ui_dump()` — verify All Categories + 13 category options
3. `capture_screenshot("subscriptions-test", "07_filter_menu")`
4. Tap "Streaming"
5. `capture_ui_dump()` — verify only Streaming subs shown + "Filtered: Streaming" text
6. `capture_screenshot("subscriptions-test", "08_filtered")`
7. Tap Filter again → "All Categories" to clear
8. Verify both subs return and chip removed

## Check 6: Validation

1. Tap FAB
2. Tap "Save Subscription" immediately (no input)
3. `capture_ui_dump()` — verify error texts:
   - "Name is required"
   - "Enter a valid amount greater than 0"
   - "Next billing date is required"
4. `capture_screenshot("subscriptions-test", "09_validation")`
5. `input_keyevent("BACK")`

## Results

| Check | Status |
|-------|--------|
| List layout correct | ? |
| Edit pre-fills and updates | ? |
| Sticky save button visible | ? |
| Search filters by prefix | ? |
| Sort reorders list | ? |
| Sort dropdown has 4 options | ? |
| Filter shows matching only | ? |
| Filter chip appears | ? |
| Filter clears correctly | ? |
| Validation: name required | ? |
| Validation: amount required | ? |
| Validation: date required | ? |
