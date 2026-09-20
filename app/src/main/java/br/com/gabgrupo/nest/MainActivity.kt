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
import br.com.gabgrupo.nest.ui.screen.OperatorIdeasScreen
import br.com.gabgrupo.nest.ui.screen.ManagerHomeScreen
import br.com.gabgrupo.nest.ui.screen.ManagerProjectsScreen
import br.com.gabgrupo.nest.ui.screen.LeaderGuidelinesScreen
import br.com.gabgrupo.nest.ui.screen.ProfileScreen
import br.com.gabgrupo.nest.ui.screen.IdeaOverviewScreen
import br.com.gabgrupo.nest.ui.screen.OperatorProjectsScreen
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.viewmodel.IdeaActionState
import br.com.gabgrupo.nest.viewmodel.IdeaListState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel
import br.com.gabgrupo.nest.viewmodel.ProjectListState
import br.com.gabgrupo.nest.viewmodel.ProjectViewModel
import dagger.hilt.android.AndroidEntryPoint
import br.com.gabgrupo.nest.data.local.SessionEvents

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
                    var userRole by rememberSaveable { mutableStateOf(UserRole.OPERATOR) }

                    LaunchedEffect(Unit) {
                        SessionEvents.expired.collect {
                            userName = ""
                            userRole = UserRole.OPERATOR
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = { role, name ->
                                    userName = name
                                    userRole = runCatching { UserRole.valueOf(role.uppercase()) }
                                        .getOrDefault(UserRole.OPERATOR)
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
                            LeaderHomeScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("leader/guidelines") {
                            LeaderGuidelinesScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("leader/projects") {
                            ManagerProjectsScreen(
                                userRole = UserRole.LEADER,
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("operator/home") {
                            OperatorHomeScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("operator/ideas") {
                            OperatorIdeasScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("operator/ideas/overview") {
                            IdeaOverviewScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("operator/projects") {
                            OperatorProjectsScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("operator/ideas/new") {
                            CreateIdeaScreen(
                                onNavigateToHatch = {
                                    navController.navigate("operator/home") {
                                        popUpTo("operator/home") { inclusive = true }
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
                                    val priority = if (status == IdeaStatus.REJECTED) null else 1
                                    ideaViewModel.reviewIdea(id, IdeaReviewRequest(status, priority))
                                },
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("manager/projects") {
                            ManagerProjectsScreen(
                                onNavigate = { route -> navController.navigate(route) }
                            )
                        }

                        composable("profile") {
                            ProfileScreen(
                                name = userName,
                                role = userRole,
                                onNavigate = { route -> navController.navigate(route) },
                                onLogout = {
                                    userName = ""
                                    userRole = UserRole.OPERATOR
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
