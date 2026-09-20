package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.IdeaStatus
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.viewmodel.IdeaListState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel

@Composable
fun IdeaOverviewScreen(
    viewModel: IdeaViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val state by viewModel.listState.collectAsState()
    var titleQuery by remember { mutableStateOf("") }
    var authorQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.getOverview() }

    Scaffold(
        topBar = { NestTopAppBar(title = { Text("Todas as ideias", color = NestNavy) }) },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.IDEAS,
                userRole = UserRole.OPERATOR,
                onNavigate = onNavigate,
                onFabClick = { onNavigate("operator/ideas/new") }
            )
        },
        containerColor = NestBackground
    ) { padding ->
        when (val current = state) {
            IdeaListState.Idle, IdeaListState.Loading -> CircularProgressIndicator(
                modifier = Modifier.padding(padding).padding(24.dp), color = NestGold
            )
            is IdeaListState.Error -> Text(current.message, color = Color.Red, modifier = Modifier.padding(padding).padding(24.dp))
            is IdeaListState.Success -> {
                val ideas = current.ideas.filter { idea ->
                    idea.title.contains(titleQuery, ignoreCase = true) &&
                        (authorQuery.isBlank() || idea.submittedBy.name.contains(authorQuery, ignoreCase = true))
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(titleQuery, { titleQuery = it }, label = { Text("Pesquisar por título") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(authorQuery, { authorQuery = it }, label = { Text("Filtrar por colaborador") }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                    items(ideas) { idea ->
                        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                            Text(idea.title, color = NestNavy, style = MaterialTheme.typography.titleMedium)
                            Text(idea.submittedBy.name, color = NestTextSecondary)
                            Text("${journeyLabel(idea.status)} | ${idea.status.name}", color = statusColor(idea.status))
                        }
                    }
                }
            }
        }
    }
}

private fun journeyLabel(status: IdeaStatus): String = when (status) {
    IdeaStatus.PENDING -> "Hatch"
    IdeaStatus.PRIORITIZED -> "Hatch"
    IdeaStatus.APPROVED -> "Flight"
    IdeaStatus.REJECTED -> "Impact"
}

private fun statusColor(status: IdeaStatus): Color = when (status) {
    IdeaStatus.PENDING -> Color(0xFFB7791F)
    IdeaStatus.PRIORITIZED -> NestGold
    IdeaStatus.APPROVED -> Color(0xFF16825D)
    IdeaStatus.REJECTED -> Color(0xFFB3261E)
}
