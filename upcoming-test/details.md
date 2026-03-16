# Upcoming Bills Test Results — March 15, 2026

## Device
- **Emulator**: emulator-5554
- **App**: com.example.subscriptiontracker
- **Note**: A competing app `com.example.subscriptioncostanalyzer` was installed and interfered with navigation. Force-stopped it before testing.

## Test Data Present
- netflix: $18.99/month (Streaming, Due today — Mar 15, 2026)
- Spotify: $9.99/month (Music, In 4 days — Mar 19, 2026)

---

## Step 1: Setup & Navigate to Upcoming Tab

1. Created test folder `upcoming-test`
2. Launched app, tapped Upcoming tab at **(541, 1780)**
3. Initially landed on wrong app (`subscriptioncostanalyzer`). Force-stopped it via `adb shell am force-stop com.example.subscriptioncostanalyzer`
4. Relaunched our app and navigated to Upcoming tab successfully
5. Captured screenshot `03_upcoming_verified_step.png`

## Step 2: Verify Total Due Summary Card

- "Total Due (30 days)" label present
- Total: **$28.98** (bold, large text)
- "2 upcoming bills" count text
- Math check: $18.99 + $9.99 = $28.98 ✓

## Step 3: Verify Bill Cards

| Subscription | Date | Days Until | Amount | Urgency |
|---|---|---|---|---|
| netflix | Mar 15, 2026 | Due today | $18.99 | Red text (≤3 days) |
| Spotify | Mar 19, 2026 | In 4 days | $9.99 | Normal text (>3 days) |

## Step 4: Verify Urgency Highlighting

- netflix "Mar 15, 2026 · Due today" displayed in **red text** (error color) — PASS
- Spotify "Mar 19, 2026 · In 4 days" displayed in normal color — PASS (correctly not highlighted)

## Step 5: Scrollability

- Scroll test disrupted by competing app interference
- Content fits on screen with 2 bills — no scroll needed

---

## Test Results Summary

| Test | Status | Notes |
|------|--------|-------|
| Upcoming Bills screen loads from bottom nav | PASS | After force-stopping competing app |
| Title "Upcoming Bills" displayed | PASS | |
| Total Due summary card present | PASS | $28.98 |
| Total equals sum of individual bills | PASS | $18.99 + $9.99 = $28.98 |
| Bill count correct | PASS | "2 upcoming bills" |
| netflix card: name, date, amount | PASS | |
| Spotify card: name, date, amount | PASS | |
| Urgency red text for ≤3 days | PASS | netflix "Due today" in red |
| Normal text for >3 days | PASS | Spotify "In 4 days" not red |

## Issues Encountered

- **Competing app**: `com.example.subscriptioncostanalyzer` was installed on the emulator and intercepted navigation/screenshots. Required `adb shell am force-stop` to resolve. This should be uninstalled from the test device.

## Screenshots

| File | Description |
|------|-------------|
| `01_upcoming_tab_step.png` | Wrong app shown (subscriptioncostanalyzer) |
| `02_upcoming_correct_step.png` | Wrong app screenshot despite correct UI dump |
| `03_upcoming_verified_step.png` | Correct Upcoming Bills screen after force-stop |
| `04_app_confirmed_step.png` | Dashboard confirming correct app in foreground |
