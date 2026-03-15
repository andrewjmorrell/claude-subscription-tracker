package com.example.subscriptiontracker.ui.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object SubscriptionList : Screen("subscription_list")
    data object AddSubscription : Screen("add_subscription")
    data object EditSubscription : Screen("edit_subscription/{id}") {
        fun createRoute(id: Long) = "edit_subscription/$id"
    }
    data object UpcomingBills : Screen("upcoming_bills")
    data object Analytics : Screen("analytics")
    data object Settings : Screen("settings")
}
