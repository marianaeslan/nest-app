package br.com.gabgrupo.nest.ui.screen.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.shared.SectionHeader
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerHomeScreen(
    userName: String = "Gestor",
    recentPendingIdeas: List<IdeaResponse> = emptyList(),
    onNavigate: (String) -> Unit,
    onNavigateToReview: (Long) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NestTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Olá, ${userName.split(" ")[0]}!",
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
                currentRoute = NavItem.HOME,
                userRole = UserRole.MANAGER,
                onNavigate = onNavigate,
                onFabClick = { onNavigate("manager/projects/new") }
            )
        },
        containerColor = NestBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SectionHeader(title = "Visão Geral")
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SummaryCard(title = "Ideias Pendentes", count = "12", modifier = Modifier.weight(1f))
                SummaryCard(title = "Projetos Ativos", count = "5", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (recentPendingIdeas.isNotEmpty()) {
                SectionHeader(title = "Últimas Ideias em Análise")
                Spacer(modifier = Modifier.height(16.dp))

                recentPendingIdeas.take(3).forEach { idea ->
                    IdeaCard(
                        title = idea.title,
                        status = UiIdeaStatus.PENDING,
                        isFavorite = false,
                        onFavoriteClick = {},
                        onClick = { onNavigateToReview(idea.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
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
        ),
        IdeaResponse(
            id = 2L,
            title = "App para comunicação interna de motoristas",
            description = "Falta um canal centralizado para comunicação.",
            status = ApiIdeaStatus.PENDING,
            priority = null,
            submittedBy = mockUser,
            reviewedBy = null,
            createdAt = "2026-05-24"
        ),
        IdeaResponse(
            id = 3L,
            title = "Otimizar carregamento de bagagens",
            description = "O processo atual gera atrasos na saída.",
            status = ApiIdeaStatus.PENDING,
            priority = null,
            submittedBy = mockUser,
            reviewedBy = null,
            createdAt = "2026-05-23"
        )
    )

    NestTheme {
        ManagerHomeScreen(
            userName = "Marcos",
            recentPendingIdeas = mockIdeas,
            onNavigate = {},
            onNavigateToReview = {}
        )
    }
}