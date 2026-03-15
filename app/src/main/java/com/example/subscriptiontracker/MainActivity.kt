package com.example.subscriptiontracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.subscriptiontracker.domain.repository.SettingsRepository
import com.example.subscriptiontracker.ui.navigation.AppNavGraph
import com.example.subscriptiontracker.ui.navigation.BottomNavItem
import com.example.subscriptiontracker.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Main entry point of the application.
 * Sets up edge-to-edge display, the Compose theme, bottom navigation bar, and navigation graph.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val darkMode by settingsRepository.getSettings()
                .map { it.darkModeEnabled }
                .collectAsStateWithLifecycle(initialValue = false)

            val currencySymbol by settingsRepository.getSettings()
                .map { it.currencySymbol }
                .collectAsStateWithLifecycle(initialValue = "$")

            AppTheme(darkTheme = darkMode) {
                MainScreen(currencySymbol = currencySymbol)
            }
        }
    }
}

/**
 * Root composable that hosts the [Scaffold] with bottom navigation and the [AppNavGraph].
 *
 * @param currencySymbol The user's preferred currency symbol for display throughout the app.
 */
@Composable
private fun MainScreen(currencySymbol: String) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show bottom bar on main tabs, not on add/edit screens
    val showBottomBar = BottomNavItem.entries.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    BottomNavItem.entries.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.screen.route
                        } == true

                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavGraph(
                navController = navController,
                currencySymbol = currencySymbol
            )
        }
    }
}
