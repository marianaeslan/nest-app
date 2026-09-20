package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import br.com.gabgrupo.nest.data.model.ProjectRequest
import br.com.gabgrupo.nest.data.model.ProjectResponse
import br.com.gabgrupo.nest.data.model.GuidelineResponse
import br.com.gabgrupo.nest.data.model.IdeaResponse
import br.com.gabgrupo.nest.data.model.IdeaStatus
import br.com.gabgrupo.nest.data.model.ProjectStage
import br.com.gabgrupo.nest.data.model.ProjectStatus
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.viewmodel.ProjectActionState
import br.com.gabgrupo.nest.viewmodel.ProjectListState
import br.com.gabgrupo.nest.viewmodel.ProjectViewModel
import br.com.gabgrupo.nest.viewmodel.GuidelineListState
import br.com.gabgrupo.nest.viewmodel.GuidelineViewModel
import br.com.gabgrupo.nest.viewmodel.IdeaListState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerProjectsScreen(
    viewModel: ProjectViewModel = hiltViewModel(),
    ideaViewModel: IdeaViewModel = hiltViewModel(),
    guidelineViewModel: GuidelineViewModel = hiltViewModel(),
    userRole: UserRole = UserRole.MANAGER,
    onNavigate: (String) -> Unit
) {
    val listState by viewModel.listState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()
    val ideaState by ideaViewModel.listState.collectAsState()
    val guidelineState by guidelineViewModel.listState.collectAsState()
    val guidelines = (guidelineState as? GuidelineListState.Success)?.guidelines.orEmpty()
    var showCreateDialog by remember { mutableStateOf(false) }
    var projectToEdit by remember { mutableStateOf<ProjectResponse?>(null) }
    var requestedProjectId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAllProjects()
        ideaViewModel.getAllIdeas()
        guidelineViewModel.getAllGuidelines()
    }

    LaunchedEffect(actionState) {
        if (actionState is ProjectActionState.Success) {
            viewModel.getAllProjects()
            viewModel.resetActionState()
            showCreateDialog = false
            projectToEdit = null
        }
    }

    Scaffold(
        topBar = {
            NestTopAppBar(title = { Text("Projetos", color = NestNavy) })
        },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.PROJECTS,
                userRole = userRole,
                onNavigate = onNavigate,
                onFabClick = { if (userRole == UserRole.MANAGER) showCreateDialog = true }
            )
        },
        floatingActionButton = if (userRole == UserRole.MANAGER) {
            {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = NestGold,
                contentColor = Color.White
            ) { Text("+") }
            }
        } else {
            {}
        },
        containerColor = NestBackground
    ) { paddingValues ->
        when (val state = listState) {
            ProjectListState.Loading, ProjectListState.Idle -> CircularProgressIndicator(
                modifier = Modifier.padding(paddingValues).padding(24.dp),
                color = NestGold
            )
            is ProjectListState.Error -> Text(
                text = state.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(paddingValues).padding(24.dp)
            )
            is ProjectListState.Success -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.projects) { project ->
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(project.title, color = NestNavy, style = MaterialTheme.typography.titleMedium)
                        Text("Status: ${project.status}", color = NestTextSecondary)
                        Text("Etapa: ${project.stage}", color = NestTextSecondary)
                        Text(
                            "Guideline: ${guidelines.firstOrNull { it.id == project.guidelineId }?.title ?: "Não vinculada"}",
                            color = NestTextSecondary
                        )
                        Text("Investimento: ${project.investment ?: "Não informado"}", color = NestTextSecondary)
                        if (userRole == UserRole.MANAGER) {
                            Button(
                                onClick = {
                                    requestedProjectId = project.id
                                    viewModel.getProjectById(project.id)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NestNavy)
                            ) { Text("Editar") }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog && userRole == UserRole.MANAGER) {
        CreateProjectDialog(
            actionState = actionState,
            approvedIdeas = (ideaState as? IdeaListState.Success)?.ideas.orEmpty()
                .filter { it.status == IdeaStatus.APPROVED },
            guidelines = guidelines,
            onDismiss = { showCreateDialog = false },
            onCreate = { title, description, investment, expectedReturn, ideaId, guidelineId ->
                viewModel.createProject(
                    ProjectRequest(
                        title = title,
                        description = description,
                        status = ProjectStatus.PLANNING,
                        stage = ProjectStage.IDEATION,
                        investment = investment,
                        expectedReturn = expectedReturn,
                        actualReturn = null,
                        productivityGain = null,
                        startDate = LocalDate.now().toString(),
                        endDate = null,
                        ideaId = ideaId,
                        guidelineId = guidelineId
                    )
                )
            }
        )
    }

    when (val detailState = viewModel.detailState.collectAsState().value) {
        is br.com.gabgrupo.nest.viewmodel.ProjectDetailState.Success -> {
            if (requestedProjectId == detailState.project.id && projectToEdit == null) {
                projectToEdit = detailState.project
            }
        }
        else -> Unit
    }

    projectToEdit?.let { project ->
        EditProjectDialog(
            project = project,
            actionState = actionState,
            onDismiss = {
                projectToEdit = null
                requestedProjectId = null
            },
            onSave = { request -> viewModel.updateProject(project.id, request) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateProjectDialog(
    actionState: ProjectActionState,
    approvedIdeas: List<IdeaResponse>,
    guidelines: List<GuidelineResponse>,
    onDismiss: () -> Unit,
    onCreate: (String, String, BigDecimal, BigDecimal, String?, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var investment by remember { mutableStateOf("") }
    var expectedReturn by remember { mutableStateOf("") }
    var selectedIdeaId by remember { mutableStateOf<String?>(null) }
    var selectedIdeaTitle by remember { mutableStateOf("Projeto independente") }
    var selectedGuidelineId by remember { mutableStateOf<String?>(null) }
    var selectedGuidelineTitle by remember { mutableStateOf("Sem guideline") }
    var ideaMenuExpanded by remember { mutableStateOf(false) }
    var guidelineMenuExpanded by remember { mutableStateOf(false) }
    val isLoading = actionState is ProjectActionState.Loading
    val error = (actionState as? ProjectActionState.Error)?.message

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo projeto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Título") }, singleLine = true)
                OutlinedTextField(description, { description = it }, label = { Text("Descrição") })
                OutlinedTextField(investment, { investment = it }, label = { Text("Investimento") }, singleLine = true)
                OutlinedTextField(expectedReturn, { expectedReturn = it }, label = { Text("Retorno esperado") }, singleLine = true)
                ExposedDropdownMenuBox(
                    expanded = ideaMenuExpanded,
                    onExpandedChange = { ideaMenuExpanded = !ideaMenuExpanded }
                ) {
                    OutlinedTextField(
                        selectedIdeaTitle,
                        {},
                        readOnly = true,
                        label = { Text("Ideia aprovada") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(ideaMenuExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(ideaMenuExpanded, { ideaMenuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Projeto independente") },
                            onClick = { selectedIdeaId = null; selectedIdeaTitle = "Projeto independente"; ideaMenuExpanded = false }
                        )
                        approvedIdeas.forEach { idea ->
                            DropdownMenuItem(
                                text = { Text(idea.title) },
                                onClick = { selectedIdeaId = idea.id; selectedIdeaTitle = idea.title; ideaMenuExpanded = false }
                            )
                        }
                    }
                }
                ExposedDropdownMenuBox(
                    expanded = guidelineMenuExpanded,
                    onExpandedChange = { guidelineMenuExpanded = !guidelineMenuExpanded }
                ) {
                    OutlinedTextField(
                        selectedGuidelineTitle,
                        {},
                        readOnly = true,
                        label = { Text("Guideline") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(guidelineMenuExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(guidelineMenuExpanded, { guidelineMenuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Sem guideline") },
                            onClick = { selectedGuidelineId = null; selectedGuidelineTitle = "Sem guideline"; guidelineMenuExpanded = false }
                        )
                        guidelines.forEach { guideline ->
                            DropdownMenuItem(
                                text = { Text(guideline.title) },
                                onClick = { selectedGuidelineId = guideline.id; selectedGuidelineTitle = guideline.title; guidelineMenuExpanded = false }
                            )
                        }
                    }
                }
                if (error != null) Text(error, color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            Button(
                enabled = !isLoading && title.isNotBlank() && description.isNotBlank(),
                onClick = {
                    val parsedInvestment = investment.toBigDecimalOrNull()
                    val parsedReturn = expectedReturn.toBigDecimalOrNull()
                    if (parsedInvestment != null && parsedReturn != null) {
                        onCreate(title, description, parsedInvestment, parsedReturn, selectedIdeaId, selectedGuidelineId)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NestGold)
            ) { if (isLoading) CircularProgressIndicator(color = NestWhite) else Text("Criar") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun EditProjectDialog(
    project: ProjectResponse,
    actionState: ProjectActionState,
    onDismiss: () -> Unit,
    onSave: (ProjectRequest) -> Unit
) {
    var title by remember(project.id) { mutableStateOf(project.title) }
    var description by remember(project.id) { mutableStateOf(project.description) }
    var actualReturn by remember(project.id) { mutableStateOf(project.actualReturn?.toString().orEmpty()) }
    var productivityGain by remember(project.id) { mutableStateOf(project.productivityGain?.toString().orEmpty()) }
    var endDate by remember(project.id) { mutableStateOf(project.endDate.orEmpty()) }
    val isLoading = actionState is ProjectActionState.Loading
    val error = (actionState as? ProjectActionState.Error)?.message

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Atualizar projeto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Título") })
                OutlinedTextField(description, { description = it }, label = { Text("Descrição") })
                OutlinedTextField(actualReturn, { actualReturn = it }, label = { Text("Retorno atual") })
                OutlinedTextField(productivityGain, { productivityGain = it }, label = { Text("Ganho de produtividade") })
                OutlinedTextField(endDate, { endDate = it }, label = { Text("Prazo (AAAA-MM-DD)") })
                if (error != null) Text(error, color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            Button(
                enabled = !isLoading && title.isNotBlank() && description.isNotBlank(),
                onClick = {
                    onSave(
                        ProjectRequest(
                            title = title,
                            description = description,
                            status = project.status,
                            stage = project.stage,
                            investment = project.investment,
                            expectedReturn = project.expectedReturn,
                            actualReturn = actualReturn.toBigDecimalOrNull(),
                            productivityGain = productivityGain.toBigDecimalOrNull(),
                            startDate = project.startDate,
                            endDate = endDate.ifBlank { null },
                            ideaId = project.ideaId,
                            guidelineId = project.guidelineId
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = NestGold)
            ) { Text("Salvar") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancelar") } }
    )
}
