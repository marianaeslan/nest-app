package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import br.com.gabgrupo.nest.data.model.IdeaRequest
import br.com.gabgrupo.nest.data.model.IdeaResponse
import br.com.gabgrupo.nest.data.model.IdeaStatus
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.viewmodel.GuidelineListState
import br.com.gabgrupo.nest.viewmodel.GuidelineViewModel
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.viewmodel.IdeaActionState
import br.com.gabgrupo.nest.viewmodel.IdeaListState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorIdeasScreen(
    viewModel: IdeaViewModel = hiltViewModel(),
    guidelineViewModel: GuidelineViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val listState by viewModel.listState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()
    val guidelineState by guidelineViewModel.listState.collectAsState()
    val guidelines = (guidelineState as? GuidelineListState.Success)?.guidelines.orEmpty()
    var ideaToEdit by remember { mutableStateOf<IdeaResponse?>(null) }

    LaunchedEffect(Unit) { viewModel.getMyIdeas() }
    LaunchedEffect(Unit) { guidelineViewModel.getAllGuidelines() }
    LaunchedEffect(actionState) {
        when (actionState) {
            is IdeaActionState.Success, IdeaActionState.Deleted -> {
                viewModel.getMyIdeas()
                viewModel.resetActionState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Minhas ideias", color = NestNavy) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NestWhite)
            )
        },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.HOME,
                userRole = UserRole.OPERATOR,
                onNavigate = onNavigate,
                onFabClick = { onNavigate("operator/ideas/new") }
            )
        },
        containerColor = NestBackground
    ) { paddingValues ->
        when (val state = listState) {
            IdeaListState.Loading, IdeaListState.Idle -> CircularProgressIndicator(
                modifier = Modifier.padding(paddingValues).padding(24.dp), color = NestGold
            )
            is IdeaListState.Error -> Text(
                state.message,
                color = Color.Red,
                modifier = Modifier.padding(paddingValues).padding(24.dp)
            )
            is IdeaListState.Success -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.ideas.sortedByDescending { it.createdAt.orEmpty() }) { idea ->
                    Card(colors = CardDefaults.cardColors(containerColor = NestWhite)) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(idea.title, color = NestNavy, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                            Text(idea.description, color = NestTextSecondary)
                            // Aplicando o label padrão do IdeaStatus
                            Text(
                                "Status: ${idea.status.label}",
                                color = when (idea.status) {
                                    IdeaStatus.PENDING -> Color(0xFFB7791F)
                                    IdeaStatus.PRIORITIZED -> NestGold
                                    IdeaStatus.APPROVED -> Color(0xFF16825D)
                                    IdeaStatus.REJECTED -> Color(0xFFB3261E)
                                }
                            )
                            Text("Criada em: ${idea.createdAt ?: "Data não informada"}", color = NestTextSecondary)
                            Text(
                                "Guideline: ${guidelines.firstOrNull { it.id == idea.guidelineId }?.title ?: "Não vinculada"}",
                                color = NestTextSecondary
                            )
                            if (idea.status == IdeaStatus.PENDING) {
                                Row {
                                    Button(onClick = { ideaToEdit = idea }, colors = ButtonDefaults.buttonColors(containerColor = NestGold)) {
                                        Text("Editar")
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Button(onClick = { viewModel.deleteIdea(idea.id) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E))) {
                                        Text("Excluir")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    ideaToEdit?.let { idea ->
        EditIdeaDialog(
            idea = idea,
            actionState = actionState,
            onDismiss = { ideaToEdit = null },
            onSave = { title, description ->
                viewModel.updateIdea(idea.id, IdeaRequest(title, description, idea.guidelineId))
                ideaToEdit = null
            }
        )
    }
}

@Composable
private fun EditIdeaDialog(
    idea: IdeaResponse,
    actionState: IdeaActionState,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember(idea.id) { mutableStateOf(idea.title) }
    var description by remember(idea.id) { mutableStateOf(idea.description) }
    val isLoading = actionState is IdeaActionState.Loading

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar ideia") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Título") })
                OutlinedTextField(description, { description = it }, label = { Text("Descrição") })
            }
        },
        confirmButton = {
            Button(
                enabled = !isLoading && title.isNotBlank() && description.isNotBlank(),
                onClick = { onSave(title, description) },
                colors = ButtonDefaults.buttonColors(containerColor = NestGold)
            ) { Text("Salvar") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancelar") } }
    )
}
