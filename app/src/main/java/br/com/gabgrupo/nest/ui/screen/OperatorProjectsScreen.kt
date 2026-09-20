package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.viewmodel.ProjectListState
import br.com.gabgrupo.nest.viewmodel.ProjectViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OperatorProjectsScreen(
    viewModel: ProjectViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val state by viewModel.listState.collectAsState()
    var selectedTab by remember { mutableStateOf("Em andamento") }

    LaunchedEffect(Unit) { viewModel.getOverview() }

    Scaffold(
        topBar = { NestTopAppBar(title = { Text("Projetos", color = NestNavy) }) },
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                color = NestGold
            )
            is ProjectListState.Error -> Text(
                current.message,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
            )
            is ProjectListState.Success -> {
                val filteredProjects = when (selectedTab) {
                    "Em andamento" -> current.projects.filter { it.status == "IN_PROGRESS" }
                    "Planejados" -> current.projects.filter { it.status == "PLANNED" }
                    "Concluídos" -> current.projects.filter { it.status == "COMPLETED" }
                    else -> current.projects
                }

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
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Em andamento", "Planejados", "Concluídos").forEach { tab ->
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

                    if (filteredProjects.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Nenhum projeto encontrado",
                                    color = NestTextSecondary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    } else {
                        items(filteredProjects) { projectSummary ->
                            ProjectSummaryCard(project = projectSummary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectSummaryCard(project: br.com.gabgrupo.nest.data.model.ProjectSummary) {
    val stageLabel = when (project.stage) {
        "IDEATION" -> "Ideação"
        "VALIDATION" -> "Validação"
        "PLANNING" -> "Planejamento"
        "EXECUTION" -> "Execução"
        "MONITORING" -> "Monitoramento"
        "COMPLETED" -> "Concluído"
        else -> project.stage
    }

    val progress = when (project.stage) {
        "IDEATION" -> 0.1f
        "VALIDATION" -> 0.25f
        "PLANNING" -> 0.4f
        "EXECUTION" -> 0.65f
        "MONITORING" -> 0.85f
        "COMPLETED" -> 1f
        else -> 0f
    }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NestWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Ícone do projeto
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = getProjectIconColor(project.id),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = project.title.firstOrNull()?.toString() ?: "P",
                            color = NestWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = NestNavy
                    )
                }

                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = NestNavy
                )
            }

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = NestNavy,
                trackColor = Color(0xFFE5E7EB)
            )

            // Informações
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Prazo",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF6B7280),
                        fontSize = 10.sp
                    )
                    Text(
                        text = project.endDate ?: "Não informado",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = NestTextSecondary,
                        fontSize = 12.sp
                    )
                }

                project.investment?.let {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Investimento",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF6B7280),
                            fontSize = 10.sp
                        )
                        Text(
                            text = currencyFormat.format(it).replace("BRL", "R$"),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = NestNavy,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Avatares de usuários
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((-8).dp)
            ) {
                // Se tem ideaId, mostra 1 bolinha (operador)
                if (!project.ideaId.isNullOrBlank()) {
                    Surface(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        color = Color(0xFF7C3AED),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "O",
                                color = NestWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                } else {
                    // Sem ideaId: 2 bolinhas (leader e manager)
                    Surface(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        color = Color(0xFF3B82F6),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "L",
                                color = NestWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        color = Color(0xFF10B981),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "M",
                                color = NestWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

        }
    }
}

private fun getProjectIconColor(projectId: String): Color {
    val colors = listOf(
        Color(0xFFFFE4CC),
        Color(0xFFCCE5FF),
        Color(0xFFCCFFE4),
        Color(0xFFFFCCE5),
        Color(0xFFFFFFCC)
    )
    return colors[projectId.hashCode() % colors.size]
}