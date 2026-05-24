package br.com.gabgrupo.nest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.gabgrupo.nest.ui.screen.CreateIdeaScreen
//import br.com.gabgrupo.nest.ui.screen.DashboardScreen
import br.com.gabgrupo.nest.ui.screen.HatchScreen
import br.com.gabgrupo.nest.ui.screen.LoginScreen
import br.com.gabgrupo.nest.ui.screen.OperatorHomeScreen
import br.com.gabgrupo.nest.ui.theme.NestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = { role ->
                                    val destination = when (role.uppercase()) {
                                        "LEADER" -> "dashboard"
                                        "GESTOR" -> "home_gestor"
                                        else -> "operator/home"
                                    }
                                    navController.navigate(destination) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("dashboard") {
                            //DashboardScreen()
                        }

                        // FLUXO OPERATOR
                        composable("operator/home") {
                            OperatorHomeScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("operator/ideas/new") {
                            CreateIdeaScreen(
                                onNavigateToHatch = {
                                    navController.navigate("operator/hatch") {
                                        popUpTo("operator/home")
                                    }
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("operator/hatch") {
                            HatchScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("home_gestor") { /* Implementação futura */ }
                    }
                }
            }
        }
    }
}