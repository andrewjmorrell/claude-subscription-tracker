# Subscriptions Test Results — March 15, 2026

## Device
- **Emulator**: emulator-5554
- **App**: com.example.subscriptiontracker

## Pre-existing Test Data
- Spotify ($9.99, Music, Monthly, In 4 days)
- netflix ($15.99, Streaming, Monthly, Due today)

---

## Step 1: Setup & Navigate to Subs Tab

1. Created test folder `subscriptions-test`
2. Verified app installed
3. Launched app — landed on Subs tab (already selected from prior state)
4. Captured screenshot `01_subs_tab_step.png`

**Observations**: Both subscriptions visible. Search bar, Sort, Filter buttons, FAB all present. Subs tab selected in bottom nav.

## Step 2: Empty State (Skipped)

Skipped — database already had 2 subscriptions from prior testing.

## Step 3-4: Add Subscriptions (Skipped)

Skipped — Spotify and netflix already present from prior testing.

## Step 5: Edit Flow

1. Tapped Netflix card at **(540, 1012)** — Edit Subscription screen opened
2. Pre-filled data confirmed: name="netflix", amount="15.99", cycle="Monthly", category="Streaming", date="Mar 15, 2026"
3. Title: "Edit Subscription", button: "Update Subscription"
4. Captured screenshot `02_edit_screen_step.png`
5. **Sticky save button verified**: "Update Subscription" visible at bottom without scrolling
6. Tapped Amount field at **(540, 578)**
7. Pressed DELETE 5 times to clear "15.99"
8. Typed "18.99"
9. Tapped "Update Subscription" at **(540, 1753)**
10. Returned to list — Netflix now shows $18.99
11. Captured screenshot `03_after_edit_step.png`

## Step 6: Search

1. Tapped search field at **(540, 389)**
2. Typed "net"
3. List filtered to show only Netflix — Spotify hidden
4. Captured screenshot `04_search_results_step.png`
5. Pressed DELETE 3 times to clear search
6. Both subscriptions returned

## Step 7: Sort

1. Tapped Sort button at **(839, 558)** — dropdown opened
2. Options shown: Name, Amount (Low → High), Amount (High → Low), Next Billing Date
3. Captured screenshot `05_sort_menu_step.png`
4. Tapped "Amount (Low → High)" at **(688, 831)**
5. List reordered: Spotify ($9.99) first, netflix ($18.99) second
6. Captured screenshot `06_sorted_step.png`

## Step 8: Category Filter

1. Tapped Filter button at **(976, 558)** — dropdown opened
2. All 14 options shown: All Categories + 13 category types
3. Captured screenshot `07_filter_menu_step.png`
4. Tapped "Streaming" at **(891, 273)**
5. List filtered to show only Netflix; "Filtered: Streaming" chip displayed
6. Captured screenshot `08_filtered_streaming_step.png`
7. Tapped Filter again, selected "All Categories" at **(891, 147)** to clear
8. Both subscriptions returned, chip removed

## Step 9: Form Validation

1. Tapped FAB at **(965, 1469)** — empty Add Subscription form opened
2. Tapped "Save Subscription" at **(541, 1753)** without filling any fields
3. Three validation errors appeared:
   - "Name is required" (red text under Name field, red border)
   - "Enter a valid amount greater than 0" (red text under Amount field, red border)
   - "Next billing date is required" (text under Date field)
4. Captured screenshot `09_validation_errors_step.png`
5. Pressed BACK to return to list

---

## Test Results Summary

| Test | Status | Notes |
|------|--------|-------|
| Subs tab loads from bottom nav | PASS | |
| Search bar, Sort, Filter buttons present | PASS | |
| FAB opens Add Subscription form | PASS | |
| Sticky save button visible without scrolling | PASS | |
| Multiple subscriptions display correctly | PASS | |
| Edit screen pre-fills existing data | PASS | Title, button text, all fields correct |
| Edit updates amount and saves | PASS | $15.99 → $18.99 |
| Search filters by name | PASS | "net" → only Netflix shown |
| Sort reorders list | PASS | Amount Low→High: Spotify first, Netflix second |
| Sort dropdown shows all 4 options | PASS | |
| Category filter shows only matching subs | PASS | Streaming filter → only Netflix |
| "Filtered:" chip appears when filtering | PASS | "Filtered: Streaming" shown |
| "All Categories" clears filter | PASS | |
| Validation: Name required | PASS | Red border + error text |
| Validation: Amount required | PASS | Red border + error text |
| Validation: Date required | PASS | Error text shown |
| BACK navigation works | PASS | |

## Screenshots

| File | Description |
|------|-------------|
| `01_subs_tab_step.png` | Subscriptions tab with Spotify and Netflix |
| `02_edit_screen_step.png` | Edit screen with Netflix pre-filled |
| `03_after_edit_step.png` | List after editing Netflix to $18.99 |
| `04_search_results_step.png` | Search "net" showing only Netflix |
| `05_sort_menu_step.png` | Sort dropdown with 4 options |
| `06_sorted_step.png` | Sorted by Amount Low→High |
| `07_filter_menu_step.png` | Filter dropdown with all categories |
| `08_filtered_streaming_step.png` | Filtered to Streaming only |
| `09_validation_errors_step.png` | Validation errors on empty save |
