package com.example.subscriptiontracker.domain.model

/** Sort options for the subscription list. */
enum class SortOption(val label: String) {
    NAME("Name"),
    AMOUNT_ASC("Amount (Low → High)"),
    AMOUNT_DESC("Amount (High → Low)"),
    NEXT_BILLING("Next Billing Date")
}
