package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import br.com.gabgrupo.nest.data.model.GuidelineRequest
import br.com.gabgrupo.nest.data.model.GuidelineResponse
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.viewmodel.GuidelineActionState
import br.com.gabgrupo.nest.viewmodel.GuidelineListState
import br.com.gabgrupo.nest.viewmodel.GuidelineViewModel
import br.com.gabgrupo.nest.viewmodel.GuidelineHistoryState

@Composable
fun LeaderGuidelinesScreen(
    viewModel: GuidelineViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val listState by viewModel.listState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()
    val historyState by viewModel.historyState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var guidelineToEdit by remember { mutableStateOf<GuidelineResponse?>(null) }
    var guidelineHistoryId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { viewModel.getAllGuidelines() }
    LaunchedEffect(actionState) {
        if (actionState is GuidelineActionState.Success) {
            viewModel.getAllGuidelines()
            viewModel.resetActionState()
            showCreateDialog = false
        }
    }

    Scaffold(
        topBar = { NestTopAppBar(title = { Text("Guidelines", color = NestNavy) }) },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.GUIDELINES,
                userRole = UserRole.LEADER,
                onNavigate = onNavigate,
                onFabClick = { showCreateDialog = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = NestGold,
                contentColor = Color.White
            ) { Text("+") }
        },
        containerColor = NestBackground
    ) { paddingValues ->
        when (val state = listState) {
            GuidelineListState.Loading, GuidelineListState.Idle -> CircularProgressIndicator(
                modifier = Modifier.padding(paddingValues).padding(24.dp), color = NestGold
            )
            is GuidelineListState.Error -> Text(
                state.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(paddingValues).padding(24.dp)
            )
            is GuidelineListState.Success -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.guidelines) { guideline ->
                    Card(colors = CardDefaults.cardColors(containerColor = NestWhite)) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(guideline.title, color = NestNavy, style = MaterialTheme.typography.titleMedium)
                            Text(guideline.content, color = NestTextSecondary)
                            Text("${guideline.category ?: "Sem categoria"} | ${guideline.campaign ?: "Sem campanha"}", color = NestTextSecondary)
                            Button(
                                onClick = { guidelineToEdit = guideline },
                                colors = ButtonDefaults.buttonColors(containerColor = NestGold)
                            ) { Text("Editar") }
                            Button(
                                onClick = {
                                    guidelineHistoryId = guideline.id
                                    viewModel.getHistory(guideline.id)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NestNavy)
                            ) { Text("Histórico") }
                            Button(
                                onClick = { viewModel.deleteGuideline(guideline.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E))
                            ) { Text("Desativar") }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateGuidelineDialog(
            actionState = actionState,
            onDismiss = { showCreateDialog = false },
            onCreate = { title, content, category, campaign ->
                viewModel.createGuideline(GuidelineRequest(title, content, category, campaign))
            }
        )
    }

    guidelineToEdit?.let { guideline ->
        EditGuidelineDialog(
            guideline = guideline,
            actionState = actionState,
            onDismiss = { guidelineToEdit = null },
            onSave = { title, content, category, campaign ->
                viewModel.updateGuideline(guideline.id, GuidelineRequest(title, content, category, campaign))
                guidelineToEdit = null
            }
        )
    }

    if (guidelineHistoryId != null) {
        GuidelineHistoryDialog(
            state = historyState,
            onDismiss = { guidelineHistoryId = null }
        )
    }
}

@Composable
private fun EditGuidelineDialog(
    guideline: GuidelineResponse,
    actionState: GuidelineActionState,
    onDismiss: () -> Unit,
    onSave: (String, String, String?, String?) -> Unit
) {
    var title by remember(guideline.id) { mutableStateOf(guideline.title) }
    var content by remember(guideline.id) { mutableStateOf(guideline.content) }
    var category by remember(guideline.id) { mutableStateOf(guideline.category.orEmpty()) }
    var campaign by remember(guideline.id) { mutableStateOf(guideline.campaign.orEmpty()) }
    val isLoading = actionState is GuidelineActionState.Loading

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar guideline") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Título") })
                OutlinedTextField(content, { content = it }, label = { Text("Conteúdo") })
                OutlinedTextField(category, { category = it }, label = { Text("Categoria") })
                OutlinedTextField(campaign, { campaign = it }, label = { Text("Campanha") })
            }
        },
        confirmButton = {
            Button(
                enabled = !isLoading && title.isNotBlank() && content.isNotBlank(),
                onClick = { onSave(title, content, category.ifBlank { null }, campaign.ifBlank { null }) },
                colors = ButtonDefaults.buttonColors(containerColor = NestGold)
            ) { Text("Salvar") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun GuidelineHistoryDialog(
    state: GuidelineHistoryState,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Histórico da guideline") },
        text = {
            when (state) {
                GuidelineHistoryState.Loading, GuidelineHistoryState.Idle -> CircularProgressIndicator(color = NestGold)
                is GuidelineHistoryState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
                is GuidelineHistoryState.Success -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (state.history.isEmpty()) Text("Nenhum snapshot encontrado.")
                    state.history.forEach { snapshot ->
                        Text(snapshot.date, color = NestTextSecondary)
                        Text(snapshot.contentSnapshot, color = NestNavy)
                    }
                }
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Fechar") } }
    )
}

@Composable
private fun CreateGuidelineDialog(
    actionState: GuidelineActionState,
    onDismiss: () -> Unit,
    onCreate: (String, String, String?, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var campaign by remember { mutableStateOf("") }
    val isLoading = actionState is GuidelineActionState.Loading

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova guideline") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Título") }, singleLine = true)
                OutlinedTextField(content, { content = it }, label = { Text("Conteúdo") })
                OutlinedTextField(category, { category = it }, label = { Text("Categoria") }, singleLine = true)
                OutlinedTextField(campaign, { campaign = it }, label = { Text("Campanha") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                enabled = !isLoading && title.isNotBlank() && content.isNotBlank(),
                onClick = { onCreate(title, content, category.ifBlank { null }, campaign.ifBlank { null }) },
                colors = ButtonDefaults.buttonColors(containerColor = NestGold)
            ) { if (isLoading) CircularProgressIndicator(color = NestWhite) else Text("Criar") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancelar") } }
    )
}
