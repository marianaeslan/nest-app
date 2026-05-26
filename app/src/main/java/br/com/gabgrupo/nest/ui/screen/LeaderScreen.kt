package br.com.gabgrupo.nest.ui.leader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.shared.ProjectCard
import br.com.gabgrupo.nest.ui.shared.SectionHeader
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.viewmodel.LeaderViewModel

private data class MockProject(
    val name: String,
    val team: String,
    val initials: String,
    val progress: Float,
    val deadline: String,
    val investment: String,
    val avatarCount: Int
)

@Composable
fun LeaderScreen(viewModel: LeaderViewModel = hiltViewModel()) {
    var currentNav by remember { mutableStateOf(NavItem.HOME) }
    val userName by viewModel.userName.collectAsState()
    val userRole by viewModel.userRole.collectAsState()

    Scaffold(
        topBar = {
            NestTopAppBar(
                title = {
                    Text(
                        text = "Olá, $userName!",
                        fontWeight = FontWeight.Bold,
                        color = NestNavy,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificações")
                    }
                }
            )
        },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = currentNav,
                userRole = userRole,
                onNavigate = { route ->
                    currentNav = when (route) {
                        "leader/dashboard" -> NavItem.HOME
                        "manager/ideas", "operator/ideas" -> NavItem.IDEAS
                        "leader/projects", "manager/projects", "operator/projects" -> NavItem.PROJECTS
                        "profile" -> NavItem.PROFILE
                        else -> NavItem.HOME
                    }
                },
                onFabClick = {}
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(NestBackground)
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionHeader(
                    title = "Projetos em Andamento",
                    actionText = "Ver todos",
                    onActionClick = {}
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderScreenPreview() {
    NestTheme {
        LeaderScreen()
    }
}