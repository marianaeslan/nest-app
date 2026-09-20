package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.viewmodel.ProjectListState
import br.com.gabgrupo.nest.viewmodel.ProjectViewModel

@Composable
fun OperatorProjectsScreen(
    viewModel: ProjectViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val state by viewModel.listState.collectAsState()
    LaunchedEffect(Unit) { viewModel.getOverview() }

    Scaffold(
        topBar = { NestTopAppBar(title = { Text("Projetos em andamento", color = NestNavy) }) },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.PROJECTS,
                userRole = UserRole.OPERATOR,
                onNavigate = onNavigate,
                onFabClick = { onNavigate("operator/ideas/new") }
            )
        },
        containerColor = NestBackground
    ) { padding ->
        when (val current = state) {
            ProjectListState.Idle, ProjectListState.Loading -> CircularProgressIndicator(
                modifier = Modifier.padding(padding).padding(24.dp), color = NestGold
            )
            is ProjectListState.Error -> Text(current.message, color = Color.Red, modifier = Modifier.padding(padding).padding(24.dp))
            is ProjectListState.Success -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(current.projects.filter { it.status == "IN_PROGRESS" }) { project ->
                    Text(project.title, color = NestNavy, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    Text("Etapa: ${project.stage}", color = NestTextSecondary)
                    Text("Prazo: ${project.endDate ?: "Não informado"}", color = NestTextSecondary)
                }
            }
        }
    }
}
