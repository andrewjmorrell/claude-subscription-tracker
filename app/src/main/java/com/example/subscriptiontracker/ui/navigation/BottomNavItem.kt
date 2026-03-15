package com.example.subscriptiontracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(val label: String, val icon: ImageVector, val screen: Screen) {
    DASHBOARD("Home", Icons.Default.Dashboard, Screen.Dashboard),
    SUBSCRIPTIONS("Subs", Icons.Default.List, Screen.SubscriptionList),
    UPCOMING("Upcoming", Icons.Default.Schedule, Screen.UpcomingBills),
    ANALYTICS("Analytics", Icons.Default.Analytics, Screen.Analytics),
    SETTINGS("Settings", Icons.Default.Settings, Screen.Settings)
}
