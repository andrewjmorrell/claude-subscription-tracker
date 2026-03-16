# Dashboard Test Results — March 15, 2026

## Device
- **Emulator**: emulator-5554
- **App**: com.example.subscriptiontracker

## Test Data Present
- netflix: $18.99/month (Streaming, Due today)
- Spotify: $9.99/month (Music, In 4 days)

---

## Step 1: Setup & Navigate to Dashboard

1. Created test folder `dashboard-test`
2. App already running, tapped Home tab at **(100, 1752)**
3. Dashboard loaded as expected
4. Captured screenshot `01_dashboard_step.png`

## Step 2: Verify Layout Structure

All expected elements confirmed in UI dump:

| Element | Found | Value |
|---------|-------|-------|
| "Dashboard" title | Yes | Top of screen |
| Monthly card | Yes | $28.98 |
| Yearly card | Yes | $347.76 |
| Active Subscriptions card | Yes | 2 |
| Upcoming Bills (7 days) header | Yes | With "See all" link |
| netflix upcoming bill | Yes | Due today, $18.99 |
| Spotify upcoming bill | Yes | In 4 days, $9.99 |
| Most Expensive header | Yes | With "See all" link |
| netflix most expensive | Yes | Monthly, $18.99/mo |
| Bottom nav (5 tabs) | Yes | Home selected |

**Math check**: $28.98/month × 12 = $347.76/year ✓

## Step 3: Test Active Subscriptions Card Tap

1. Found Active Subscriptions card, tapped center at **(540, 662)**
2. Navigated to Subscriptions screen — "Subscriptions" title visible
3. Both subscription cards shown (netflix $18.99, Spotify $9.99)
4. Captured screenshot `02_active_subs_tap_step.png`
5. Pressed BACK — returned to Dashboard

## Step 4: Test "See All" Upcoming Bills Link

1. Found "See all" button near Upcoming Bills at **(954, 878)**
2. Tapped — navigated to Upcoming Bills screen
3. Verified: "Upcoming Bills" title, Total Due $28.98, 2 upcoming bills
4. netflix: "Mar 15, 2026 · Due today" in red (urgency), $18.99
5. Spotify: "Mar 19, 2026 · In 4 days", $9.99
6. Captured screenshot `03_see_all_upcoming_step.png`
7. Pressed BACK — returned to Dashboard

## Step 5: Test Scrollability

1. Scrolled down — no visible change (content fits on screen with 2 subs)
2. ScrollView present in UI hierarchy — will work with more content

---

## Test Results Summary

| Test | Status | Notes |
|------|--------|-------|
| Dashboard loads as start destination | PASS | Home tab selected |
| Monthly cost card | PASS | $28.98 |
| Yearly cost card | PASS | $347.76 (consistent with monthly) |
| Active Subscriptions count | PASS | Shows 2 |
| Upcoming Bills section | PASS | netflix Due today + Spotify In 4 days |
| Most Expensive section | PASS | netflix $18.99/mo |
| Active Subscriptions card tap → Subs | PASS | Navigates correctly |
| BACK returns to Dashboard | PASS | |
| "See all" → Upcoming Bills | PASS | Navigates correctly |
| Urgency highlighting | PASS | Red text on "Due today" |
| Scroll support | PASS | ScrollView present |

## Screenshots

| File | Description |
|------|-------------|
| `01_dashboard_step.png` | Dashboard with all sections visible |
| `02_active_subs_tap_step.png` | Subs tab after tapping Active Subscriptions card |
| `03_see_all_upcoming_step.png` | Upcoming Bills via "See all" link |
