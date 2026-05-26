package br.com.gabgrupo.nest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.gabgrupo.nest.data.model.IdeaReviewRequest
import br.com.gabgrupo.nest.ui.screen.CreateIdeaScreen
import br.com.gabgrupo.nest.ui.screen.HatchScreen
import br.com.gabgrupo.nest.ui.screen.LoginScreen
import br.com.gabgrupo.nest.ui.screen.OperatorHomeScreen
import br.com.gabgrupo.nest.ui.screen.manager.AllIdeasScreen
import br.com.gabgrupo.nest.ui.screen.manager.ManagerHomeScreen
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.viewmodel.IdeaListState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel
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
                    var userName by rememberSaveable { mutableStateOf("") }

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = { role, name ->
                                    userName = name
                                    val destination = when (role.uppercase()) {
                                        "LEADER" -> "dashboard"
                                        "GESTOR", "MANAGER" -> "manager/home"
                                        else -> "operator/home"
                                    }
                                    navController.navigate(destination) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("dashboard") {

                        }

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

                        composable("manager/home") {
                            val viewModel: IdeaViewModel = hiltViewModel()
                            val listState by viewModel.listState.collectAsState()

                            LaunchedEffect(Unit) {
                                viewModel.getAllIdeas()
                            }

                            val ideas = (listState as? IdeaListState.Success)?.ideas ?: emptyList()
                            val recentPendingIdeas = ideas.filter { it.status.name == "PENDING" }

                            ManagerHomeScreen(
                                userName = userName.ifBlank { "Gestor" },
                                recentPendingIdeas = recentPendingIdeas,
                                onNavigate = { route -> navController.navigate(route) },
                                onNavigateToReview = { ideaId ->
                                    navController.navigate("manager/ideas/$ideaId")
                                }
                            )
                        }

                        composable("manager/ideas") {
                            val viewModel: IdeaViewModel = hiltViewModel()
                            val listState by viewModel.listState.collectAsState()

                            LaunchedEffect(Unit) {
                                viewModel.getAllIdeas()
                            }

                            val ideas = (listState as? IdeaListState.Success)?.ideas ?: emptyList()

                            AllIdeasScreen(
                                ideas = ideas,
                                onNavigate = { route -> navController.navigate(route) },
                                onSubmitReview = { id, status ->
                                    viewModel.reviewIdea(
                                        id = id,
                                        request = IdeaReviewRequest(
                                            status = status,
                                            priority = 0
                                        )
                                    )
                                    viewModel.getAllIdeas()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}