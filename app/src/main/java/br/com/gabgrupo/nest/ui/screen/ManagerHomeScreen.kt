package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.gabgrupo.nest.data.model.IdeaResponse
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.data.model.UserSummary
import br.com.gabgrupo.nest.data.model.IdeaStatus as ApiIdeaStatus
import br.com.gabgrupo.nest.ui.shared.IdeaCard
import br.com.gabgrupo.nest.ui.shared.IdeaStatus as UiIdeaStatus
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTabRow
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.shared.SectionHeader
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerHomeScreen(
    userName: String = "Gestor",
    pendingIdeasCount: Int = 0,
    activeProjectsCount: Int = 0,
    ideas: List<IdeaResponse> = emptyList(),
    onSubmitReview: (Long, ApiIdeaStatus) -> Unit,
    onNavigate: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var ideaToReview by remember { mutableStateOf<IdeaResponse?>(null) }

    val tabs = listOf("Pendentes", "Todas", "Aprovadas")
    val firstName = userName.split(" ").firstOrNull() ?: userName

    val formattedDate = remember(ideaToReview?.createdAt) {
        ideaToReview?.createdAt?.let { rawDate ->
            try {
                val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
                val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss")
                val parsed = inputFormatter.parseBest(rawDate, ZonedDateTime::from, LocalDateTime::from, LocalDate::from)
                
                when (parsed) {
                    is ZonedDateTime -> parsed.format(outputFormatter)
                    is LocalDateTime -> parsed.format(outputFormatter)
                    is LocalDate -> parsed.atStartOfDay().format(outputFormatter)
                    else -> rawDate
                }
            } catch (e: Exception) {
                rawDate
            }
        } ?: ""
    }

    val filteredIdeas = ideas.filter { idea ->
        when (selectedTabIndex) {
            0 -> idea.status == ApiIdeaStatus.PENDING
            1 -> true
            2 -> idea.status == ApiIdeaStatus.APPROVED || idea.status == ApiIdeaStatus.PRIORITIZED
            else -> true
        }
    }

    if (ideaToReview != null) {
        val isPending = ideaToReview?.status == ApiIdeaStatus.PENDING

        AlertDialog(
            onDismissRequest = { ideaToReview = null },
            containerColor = NestWhite,
            title = {
                Text(
                    text = ideaToReview?.title.orEmpty(),
                    color = NestNavy,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    // Autor Section
                    Column {
                        Text(
                            text = "Autor",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = NestNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ideaToReview?.submittedBy?.name ?: "Não informado",
                            style = MaterialTheme.typography.bodyLarge,
                            color = NestNavy,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Status Section
                    if (!isPending) {
                        Column {
                            Text(
                                text = "Status",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = NestNavy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = ideaToReview?.status?.name ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NestGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Descrição Section
                    Column {
                        Text(
                            text = "Descrição",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = NestNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ideaToReview?.description.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = NestTextSecondary,
                            lineHeight = 20.sp
                        )
                    }

                    // Data Section
                    Column {
                        Text(
                            text = "Data de envio",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = NestNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = NestTextSecondary
                        )
                    }
                }
            },
            confirmButton = {
                if (isPending) {
                    Button(
                        onClick = {
                            onSubmitReview(ideaToReview!!.id, ApiIdeaStatus.APPROVED)
                            ideaToReview = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Text("Aprovar", color = NestWhite)
                    }
                } else {
                    Button(
                        onClick = { ideaToReview = null },
                        colors = ButtonDefaults.buttonColors(containerColor = NestNavy)
                    ) {
                        Text("Fechar", color = NestWhite)
                    }
                }
            },
            dismissButton = {
                if (isPending) {
                    Button(
                        onClick = {
                            onSubmitReview(ideaToReview!!.id, ApiIdeaStatus.REJECTED)
                            ideaToReview = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                    ) {
                        Text("Rejeitar", color = NestWhite)
                    }
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NestTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Olá, $firstName!",
                            fontWeight = FontWeight.Bold,
                            color = NestNavy,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Que bom te ver por aqui.",
                            color = NestTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )
        },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.IDEAS,
                userRole = UserRole.MANAGER,
                onNavigate = onNavigate,
                onFabClick = { onNavigate("manager/projects/new") }
            )
        },
        containerColor = NestBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    SectionHeader(title = "Visão Geral")
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SummaryCard(
                            title = "Ideias Pendentes",
                            count = pendingIdeasCount.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Projetos Ativos",
                            count = activeProjectsCount.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    SectionHeader(title = "Gestão de Ideias")
                    
                    NestTabRow(
                        tabs = tabs,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it },
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

            items(filteredIdeas) { idea ->
                val ideaStatus = when (idea.status) {
                    ApiIdeaStatus.PENDING -> UiIdeaStatus.PENDING
                    ApiIdeaStatus.APPROVED,
                    ApiIdeaStatus.PRIORITIZED -> UiIdeaStatus.APPROVED
                    ApiIdeaStatus.REJECTED -> UiIdeaStatus.REJECTED
                }

                IdeaCard(
                    title = idea.title,
                    status = ideaStatus,
                    isFavorite = false,
                    onFavoriteClick = { },
                    onClick = { ideaToReview = idea },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SummaryCard(title: String, count: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = NestWhite)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NestNavy)
            Text(text = title, fontSize = 12.sp, color = NestTextSecondary)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ManagerHomeScreenPreview() {
    val mockUser = UserSummary(id = 1L, name = "Colaborador Teste", role = UserRole.OPERATOR)
    val mockIdeas = listOf(
        IdeaResponse(
            id = 1L,
            title = "Reduzir tempo de check-in dos passageiros",
            description = "Durante os horários de pico, o check-in manual causa filas.",
            status = ApiIdeaStatus.PENDING,
            priority = null,
            submittedBy = mockUser,
            reviewedBy = null,
            createdAt = "2026-05-25"
        )
    )

    NestTheme {
        ManagerHomeScreen(
            userName = "Marcos",
            pendingIdeasCount = 12,
            activeProjectsCount = 5,
            ideas = mockIdeas,
            onSubmitReview = { _, _ -> },
            onNavigate = {}
        )
    }
}
