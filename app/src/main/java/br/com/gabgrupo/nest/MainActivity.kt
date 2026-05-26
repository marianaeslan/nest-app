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
import br.com.gabgrupo.nest.data.model.IdeaStatus
import br.com.gabgrupo.nest.ui.leader.LeaderHomeScreen
import br.com.gabgrupo.nest.ui.screen.CreateIdeaScreen
import br.com.gabgrupo.nest.ui.screen.HatchScreen
import br.com.gabgrupo.nest.ui.screen.LoginScreen
import br.com.gabgrupo.nest.ui.screen.OperatorHomeScreen
import br.com.gabgrupo.nest.ui.screen.ManagerHomeScreen
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.viewmodel.IdeaActionState
import br.com.gabgrupo.nest.viewmodel.IdeaListState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel
import br.com.gabgrupo.nest.viewmodel.ProjectListState
import br.com.gabgrupo.nest.viewmodel.ProjectViewModel
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
                            LeaderHomeScreen()

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
                            val ideaViewModel: IdeaViewModel = hiltViewModel()
                            val projectViewModel: ProjectViewModel = hiltViewModel()
                            
                            val ideaState by ideaViewModel.listState.collectAsState()
                            val projectState by projectViewModel.listState.collectAsState()
                            val actionState by ideaViewModel.actionState.collectAsState()

                            LaunchedEffect(actionState) {
                                if (actionState is IdeaActionState.Success) {
                                    ideaViewModel.getAllIdeas()
                                    projectViewModel.getAllProjects()
                                    ideaViewModel.resetActionState()
                                }
                            }

                            LaunchedEffect(Unit) {
                                ideaViewModel.getAllIdeas()
                                projectViewModel.getAllProjects()
                            }

                            val ideas = (ideaState as? IdeaListState.Success)?.ideas ?: emptyList()
                            val pendingIdeasCount = ideas.count { it.status == IdeaStatus.PENDING }
                            
                            val projects = (projectState as? ProjectListState.Success)?.projects ?: emptyList()
                            val activeProjectsCount = projects.filter { it.status == "IN_PROGRESS" }.size

                            ManagerHomeScreen(
                                userName = userName.ifBlank { "Gestor" },
                                pendingIdeasCount = pendingIdeasCount,
                                activeProjectsCount = activeProjectsCount,
                                ideas = ideas,
                                onSubmitReview = { id, status ->
                                    ideaViewModel.reviewIdea(id, IdeaReviewRequest(status, 1))
                                },
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }
                    }
                }
            }
        }
    }
}
