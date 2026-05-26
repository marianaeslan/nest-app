package br.com.gabgrupo.nest.ui.screen.manager

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import br.com.gabgrupo.nest.data.model.IdeaResponse
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.data.model.UserSummary
import br.com.gabgrupo.nest.data.model.IdeaStatus as ApiIdeaStatus
import br.com.gabgrupo.nest.ui.shared.IdeaCard
import br.com.gabgrupo.nest.ui.shared.IdeaStatus as UiIdeaStatus
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTabRow
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary

@Composable
fun AllIdeasScreen(
    ideas: List<IdeaResponse>,
    onSubmitReview: (Long, ApiIdeaStatus) -> Unit,
    onNavigate: (String) -> Unit
) {
    AllIdeasScreenContent(
        ideas = ideas,
        onSubmitReview = onSubmitReview,
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllIdeasScreenContent(
    ideas: List<IdeaResponse>,
    onSubmitReview: (Long, ApiIdeaStatus) -> Unit,
    onNavigate: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var ideaToReview by remember { mutableStateOf<IdeaResponse?>(null) }

    val tabs = listOf("Pendentes", "Todas", "Aprovadas")

    val filteredIdeas = ideas.filter { idea ->
        when (selectedTabIndex) {
            0 -> idea.status == ApiIdeaStatus.PENDING
            1 -> true
            2 -> idea.status == ApiIdeaStatus.APPROVED || idea.status == ApiIdeaStatus.PRIORITIZED
            else -> true
        }
    }

    if (ideaToReview != null) {
        AlertDialog(
            onDismissRequest = {
                ideaToReview = null
            },
            containerColor = NestWhite,
            title = {
                Text(
                    text = ideaToReview?.title.orEmpty(),
                    color = NestNavy,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = ideaToReview?.description.orEmpty(),
                        color = NestTextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSubmitReview(ideaToReview!!.id, ApiIdeaStatus.APPROVED)
                        ideaToReview = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Aprovar", color = NestWhite)
                }
            },
            dismissButton = {
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
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Ideias", fontWeight = FontWeight.Bold, color = NestNavy) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NestWhite)
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
        Column(modifier = Modifier.padding(paddingValues)) {
            NestTabRow(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                modifier = Modifier.padding(24.dp)
            )

            LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
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
                        onClick = { ideaToReview = idea }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AllIdeasScreenPreview() {
    val mockUser = UserSummary(id = 1L, name = "Colaborador Teste", role = UserRole.OPERATOR)
    val mockIdeas = listOf(
        IdeaResponse(
            id = 1L,
            title = "Reduzir tempo de check-in dos passageiros",
            description = "Descrição detalhada do problema",
            status = ApiIdeaStatus.PENDING,
            priority = null,
            submittedBy = mockUser,
            reviewedBy = null,
            createdAt = "2026-05-25"
        ),
        IdeaResponse(
            id = 2L,
            title = "Melhoria no processo de limpeza dos ônibus",
            description = "Descrição detalhada do problema da limpeza",
            status = ApiIdeaStatus.APPROVED,
            priority = 75,
            submittedBy = mockUser,
            reviewedBy = null,
            createdAt = "2026-05-24"
        )
    )

    NestTheme {
        AllIdeasScreenContent(
            ideas = mockIdeas,
            onSubmitReview = { _, _ -> },
            onNavigate = {}
        )
    }
}