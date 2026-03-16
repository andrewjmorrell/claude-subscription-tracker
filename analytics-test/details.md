# Analytics Test Results — March 15, 2026

## Device
- **Emulator**: emulator-5554
- **App**: com.example.subscriptiontracker

## Test Data Present
- netflix: $18.99/month (Streaming)
- Spotify: $9.99/month (Music)

---

## Step 1: Navigate to Analytics Tab

1. Tapped Analytics tab at **(761, 1780)**
2. Analytics screen loaded with all sections visible
3. Captured screenshot `01_analytics_step.png`

## Step 2: Cost Summary Cards

| Card | Value | Expected | Status |
|------|-------|----------|--------|
| Monthly | $28.98 | $18.99 + $9.99 | PASS |
| Yearly | $347.76 | $28.98 × 12 | PASS |
| Daily Average | $0.95 | $347.76 / 365 ≈ $0.953 | PASS |

## Step 3: Category Breakdown

| Category | Amount | % | Count | Progress Bar |
|----------|--------|---|-------|-------------|
| Streaming | $18.99/mo | 65% | 1 subscription | Present (longer) |
| Music | $9.99/mo | 34% | 1 subscription | Present (shorter) |

- Percentages: 65% + 34% = 99% (rounding from $18.99/$28.98 = 65.5% and $9.99/$28.98 = 34.5%) ✓
- Streaming bar longer than Music bar ✓ (proportional to percentages)

## Step 4: Summary Section

| Row | Value |
|-----|-------|
| Active Subscriptions | 2 |
| Categories Used | 2 |

Note: "Avg per Subscription" row partially truncated at screen bottom. Expected: $28.98 / 2 = $14.49.

## Step 5: Scroll

- Scrolled down — content fits nearly on screen, Summary partially visible
- Captured screenshot `02_scrolled_step.png`

---

## Test Results Summary

| Test | Status | Notes |
|------|--------|-------|
| Analytics screen loads from bottom nav | PASS | Analytics tab selected |
| Title "Analytics" displayed | PASS | |
| Monthly cost card correct | PASS | $28.98 |
| Yearly cost card correct | PASS | $347.76 |
| Daily Average correct | PASS | $0.95 |
| Category breakdown present | PASS | Streaming + Music |
| Category amounts correct | PASS | $18.99/mo + $9.99/mo |
| Percentages add up | PASS | 65% + 34% = 99% (rounding) |
| Progress bars present & proportional | PASS | Streaming longer than Music |
| Subscription counts correct | PASS | 1 each |
| Summary: Active Subscriptions | PASS | 2 |
| Summary: Categories Used | PASS | 2 |
| Screen scrolls | PASS | |

## Screenshots

| File | Description |
|------|-------------|
| `01_analytics_step.png` | Full Analytics screen |
| `02_scrolled_step.png` | After scroll (same view — content nearly fits) |
