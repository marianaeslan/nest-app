package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.IdeaRequest
import br.com.gabgrupo.nest.data.model.IdeaResponse
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
import br.com.gabgrupo.nest.viewmodel.IdeaActionState
import br.com.gabgrupo.nest.viewmodel.IdeaListState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel

@Composable
fun IdeaOverviewScreen(
    viewModel: IdeaViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val state by viewModel.listState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()
    var titleQuery by remember { mutableStateOf("") }
    var selectedAuthor by remember { mutableStateOf<String?>(null) }
    var selectedIdea by remember { mutableStateOf<IdeaResponse?>(null) }
    var ideaToEdit by remember { mutableStateOf<IdeaResponse?>(null) }
    var selectedTab by remember { mutableStateOf("Todas") }
    var showFilterMenu by remember { mutableStateOf(false) }

    LaunchedEffect(actionState) {
        when (actionState) {
            is IdeaActionState.Success, IdeaActionState.Deleted -> {
                viewModel.getOverview()
                viewModel.resetActionState()
                selectedIdea = null
                ideaToEdit = null
            }
            else -> Unit
        }
    }

    LaunchedEffect(Unit) { viewModel.getOverview() }

    Scaffold(
        topBar = { NestTopAppBar(title = { Text("Ideias", color = NestNavy) }) },
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                color = NestGold
            )
            is IdeaListState.Error -> Text(
                current.message,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
            )
            is IdeaListState.Success -> {
                val ideas = current.ideas.filter { idea ->
                    idea.title.contains(titleQuery, ignoreCase = true) &&
                            (selectedAuthor == null || idea.submittedBy.name == selectedAuthor)
                }

                val uniqueAuthors = current.ideas.map { it.submittedBy.name }.distinct().sorted()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = NestTextSecondary,
                                modifier = Modifier.padding(start = 4.dp)
                            )

                            TextField(
                                value = titleQuery,
                                onValueChange = { titleQuery = it },
                                placeholder = { Text("Buscar ideias", color = Color(0xFFAAAAAA)) },
                                modifier = Modifier
                                    .weight(1f),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                )
                            )

                            Box {
                                IconButton(
                                    onClick = { showFilterMenu = true },
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = "Filtrar",
                                        tint = NestNavy,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showFilterMenu,
                                    onDismissRequest = { showFilterMenu = false },
                                    modifier = Modifier.background(NestWhite)
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Todos os colaboradores",
                                                color = if (selectedAuthor == null) NestNavy else NestTextSecondary,
                                                fontWeight = if (selectedAuthor == null) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            selectedAuthor = null
                                            showFilterMenu = false
                                        }
                                    )

                                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                                    uniqueAuthors.forEach { author ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    author,
                                                    color = if (selectedAuthor == author) NestNavy else NestTextSecondary,
                                                    fontWeight = if (selectedAuthor == author) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            onClick = {
                                                selectedAuthor = author
                                                showFilterMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Minhas ideias", "Todas").forEach { tab ->
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(24.dp))
                                        .clickable { selectedTab = tab },
                                    color = if (selectedTab == tab) NestNavy else Color(0xFFE8E8E8),
                                    shape = RoundedCornerShape(24.dp)
                                ) {
                                    Text(
                                        tab,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        color = if (selectedTab == tab) NestWhite else NestNavy,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    if (ideas.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Nenhuma ideia encontrada",
                                    color = NestTextSecondary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    } else {
                        items(ideas) { idea ->
                            IdeaCard(
                                idea = idea,
                                onCardClick = { selectedIdea = idea }
                            )
                        }
                    }
                }
            }
        }
    }

    if (selectedIdea != null) {
        IdeaDetailsDialog(
            idea = selectedIdea!!,
            actionState = actionState,
            onDismiss = { selectedIdea = null },
            onEdit = { ideaToEdit = selectedIdea },
            onDelete = { viewModel.deleteIdea(selectedIdea!!.id) }
        )
    }

    ideaToEdit?.let { idea ->
        EditIdeaDialog(
            idea = idea,
            actionState = actionState,
            onDismiss = { ideaToEdit = null },
            onSave = { title, description ->
                viewModel.updateIdea(idea.id, IdeaRequest(title, description, idea.guidelineId))
            }
        )
    }
}

@Composable
private fun IdeaCard(
    idea: IdeaResponse,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NestWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = idea.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NestNavy,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = idea.submittedBy.name,
                    fontSize = 12.sp,
                    color = NestTextSecondary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp)),
                    color = statusBackgroundColor(idea.status),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = idea.status.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor(idea.status),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun IdeaDetailsDialog(
    idea: IdeaResponse,
    actionState: IdeaActionState,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(20.dp)),
            color = NestWhite
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalhes da Ideia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NestNavy
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = NestNavy
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                DetailItem(label = "Título", value = idea.title)
                DetailItem(label = "Descrição", value = idea.description)
                DetailItem(
                    label = "Status",
                    value = idea.status.label,
                    valueColor = statusColor(idea.status)
                )
                DetailItem(
                    label = "Guideline",
                    value = idea.guidelineId ?: "Nenhuma selecionada"
                )
                DetailItem(
                    label = "Prioridade",
                    value = idea.priority?.toString() ?: "Não definida"
                )
                DetailItem(
                    label = "Autor",
                    value = idea.submittedBy.name
                )
                DetailItem(
                    label = "Data de submissão",
                    value = idea.createdAt?.substringBefore("T") ?: "Não disponível"
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botões de ação - apenas se status for PENDING
                if (idea.status == IdeaStatus.PENDING) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onEdit,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = NestGold),
                            shape = RoundedCornerShape(12.dp),
                            enabled = actionState !is IdeaActionState.Loading
                        ) {
                            Text("Editar", color = NestNavy)
                        }
                        Button(
                            onClick = onDelete,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E)),
                            shape = RoundedCornerShape(12.dp),
                            enabled = actionState !is IdeaActionState.Loading
                        ) {
                            Text("Excluir", color = NestWhite)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NestNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Fechar", color = NestWhite)
                }
            }
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String,
    valueColor: Color = NestNavy
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = NestTextSecondary,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 15.sp,
            color = valueColor,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color(0xFFF1F1F1))
    }
}

private fun statusColor(status: IdeaStatus): Color = when (status) {
    IdeaStatus.PENDING -> Color(0xFFB7791F)
    IdeaStatus.PRIORITIZED -> NestGold
    IdeaStatus.APPROVED -> Color(0xFF16825D)
    IdeaStatus.REJECTED -> Color(0xFFB3261E)
}

private fun statusBackgroundColor(status: IdeaStatus): Color = when (status) {
    IdeaStatus.PENDING -> Color(0xFFFEF3C7)
    IdeaStatus.PRIORITIZED -> Color(0xFFFEF3C7)
    IdeaStatus.APPROVED -> Color(0xFFDCFCE7)
    IdeaStatus.REJECTED -> Color(0xFFFEE2E2)
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
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth()
                )
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