# Analytics Test Script

Rerunnable test. Execute steps in order. Report PASS/FAIL for each check.

## Setup

1. `create_test_folder("analytics-test")`
2. `list_apps("subscriptiontracker")` — if missing, stop
3. `open_app("com.example.subscriptiontracker")`
4. `capture_ui_dump()` — find "Analytics" tab, tap its center
5. `capture_screenshot("analytics-test", "01_analytics")`

## Check 1: Cost Summary Cards

`capture_ui_dump()` — verify:
- "Monthly" with dollar amount
- "Yearly" with dollar amount (should = monthly × 12)
- "Daily Average" with dollar amount (should ≈ yearly / 365)

## Check 2: Category Breakdown

Verify "Spending by Category" section:
- Each category row has: name, "$X.XX/mo", ProgressBar, "NN%", "N subscription(s)"
- Percentages should add up to ~100%
- Streaming bar should be longer than Music bar (if both present)
- `capture_screenshot("analytics-test", "02_categories")`

## Check 3: Summary Section

`input_scroll("down")` if needed, then `capture_ui_dump()`:
- "Active Subscriptions" with count
- "Categories Used" with count
- "Avg per Subscription" with amount (= monthly / active count)
- `capture_screenshot("analytics-test", "03_summary")`

## Check 4: Scroll

1. `input_scroll("up")` to top
2. `input_scroll("down")` to bottom
3. `capture_screenshot("analytics-test", "04_scroll")`
4. PASS if all content accessible

## Results

| Check | Status |
|-------|--------|
| Analytics screen loads | ? |
| Title displayed | ? |
| Monthly card correct | ? |
| Yearly = monthly × 12 | ? |
| Daily average ≈ yearly / 365 | ? |
| Categories with amounts | ? |
| Percentages add up | ? |
| Progress bars proportional | ? |
| Summary: active count | ? |
| Summary: categories count | ? |
| Scroll works | ? |
