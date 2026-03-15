package com.example.subscriptiontracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.subscriptiontracker.ui.screen.addedit.AddEditSubscriptionScreen
import com.example.subscriptiontracker.ui.screen.analytics.AnalyticsScreen
import com.example.subscriptiontracker.ui.screen.dashboard.DashboardScreen
import com.example.subscriptiontracker.ui.screen.settings.SettingsScreen
import com.example.subscriptiontracker.ui.screen.subscriptionlist.SubscriptionListScreen
import com.example.subscriptiontracker.ui.screen.upcoming.UpcomingBillsScreen

/**
 * Defines the full navigation graph for the application.
 * Each [Screen] route is mapped to its corresponding composable screen.
 *
 * @param navController The [NavHostController] managing back stack and navigation.
 * @param currencySymbol The user's preferred currency symbol, passed to screens for display.
 */
@Composable
fun AppNavGraph(navController: NavHostController, currencySymbol: String) {
    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                currencySymbol = currencySymbol,
                onNavigateToSubscriptions = {
                    navController.navigate(Screen.SubscriptionList.route)
                },
                onNavigateToUpcoming = {
                    navController.navigate(Screen.UpcomingBills.route)
                }
            )
        }

        composable(Screen.SubscriptionList.route) {
            SubscriptionListScreen(
                currencySymbol = currencySymbol,
                onAddClick = { navController.navigate(Screen.AddSubscription.route) },
                onEditClick = { id ->
                    navController.navigate(Screen.EditSubscription.createRoute(id))
                }
            )
        }

        composable(Screen.AddSubscription.route) {
            AddEditSubscriptionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditSubscription.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            AddEditSubscriptionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.UpcomingBills.route) {
            UpcomingBillsScreen(currencySymbol = currencySymbol)
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen(currencySymbol = currencySymbol)
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
