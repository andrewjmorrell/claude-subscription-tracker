# Upcoming Bills Test Script

Rerunnable test. Execute steps in order. Report PASS/FAIL for each check.

## Pre-check

Force-stop competing app if present:
```
adb shell am force-stop com.example.subscriptioncostanalyzer
```

## Setup

1. `create_test_folder("upcoming-test")`
2. `list_apps("subscriptiontracker")` — if missing, stop
3. `open_app("com.example.subscriptiontracker")`
4. `capture_ui_dump()` — find "Upcoming" tab, tap its center
5. `capture_screenshot("upcoming-test", "01_upcoming_tab")`
6. `capture_ui_dump()` — verify "Upcoming Bills" title present

## Check 1: Total Due Summary Card

Verify in UI dump:
- "Total Due (30 days)" text present
- Dollar amount in large text
- "N upcoming bill(s)" count text
- PASS if all present; verify total = sum of individual bill amounts

## Check 2: Bill Cards

For each bill card, verify:
- Subscription name
- Date + "days until" text (e.g., "Mar 15, 2026 · Due today")
- Amount on the right
- `capture_screenshot("upcoming-test", "02_bill_cards")`

## Check 3: Urgency Highlighting

- Bills due within 3 days: date text should be in error/red color
- Bills due after 3 days: normal color
- PASS if urgency styling matches due dates

## Check 4: Scrollability

1. `input_scroll("down")`
2. `capture_screenshot("upcoming-test", "03_scrolled")`
3. `input_scroll("up")`
4. PASS if scroll accepted

## Results

| Check | Status |
|-------|--------|
| Upcoming Bills screen loads | ? |
| Title displayed | ? |
| Total Due card present | ? |
| Total equals sum of bills | ? |
| Bill count correct | ? |
| Each bill has name, date, amount | ? |
| Urgency red text for ≤3 days | ? |
| Normal text for >3 days | ? |
| Scroll works | ? |
