package br.com.gabgrupo.nest.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestWhite

enum class NavItem(val title: String, val icon: ImageVector) {
    HOME("Início", Icons.Default.Home),
    IDEAS("Ideias", Icons.Default.Lightbulb),
    PROJECTS("Projetos", Icons.Default.Folder),
    GUIDELINES("Guidelines", Icons.Default.Lightbulb),
    USERS("Usuários", Icons.Default.People),
    PROFILE("Perfil", Icons.Default.Person)
}

@Composable
fun NestBottomNavBar(
    currentRoute: NavItem,
    userRole: UserRole,
    onNavigate: (String) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxWidth()) {
        NavigationBar(
            containerColor = NestWhite,
            contentColor = NestTextSecondary
        ) {
            val items = when (userRole) {
                UserRole.LEADER -> listOf(NavItem.HOME, NavItem.PROJECTS, NavItem.GUIDELINES, NavItem.PROFILE)
                UserRole.MANAGER -> listOf(NavItem.HOME, NavItem.IDEAS, NavItem.PROJECTS, NavItem.PROFILE)
                else -> listOf(NavItem.HOME, NavItem.IDEAS, NavItem.PROJECTS, NavItem.PROFILE)
            }

            items.forEach { item ->
                NestNavItem(
                    item = item,
                    isSelected = currentRoute == item,
                    userRole = userRole,
                    onNavigate = onNavigate
                )
            }
        }

        if (userRole == UserRole.OPERATOR) {
            NestFab(
                onClick = onFabClick,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun RowScope.NestNavItem(
    item: NavItem,
    isSelected: Boolean,
    userRole: UserRole,
    onNavigate: (String) -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = {
            val route = when (item) {
                NavItem.HOME -> when (userRole) {
                    UserRole.OPERATOR -> "operator/home"
                    UserRole.MANAGER -> "manager/home"
                    UserRole.LEADER -> "dashboard"
                }
                NavItem.IDEAS -> when (userRole) {
                    UserRole.OPERATOR -> "operator/ideas"
                    UserRole.MANAGER -> "manager/home"
                    UserRole.LEADER -> "dashboard"
                }
                NavItem.PROJECTS -> when (userRole) {
                    UserRole.OPERATOR -> "operator/projects"
                    UserRole.MANAGER -> "manager/projects"
                    UserRole.LEADER -> "leader/projects"
                }
                NavItem.GUIDELINES -> "leader/guidelines"
                NavItem.USERS -> "leader/users"
                NavItem.PROFILE -> "profile"
            }
            onNavigate(route)
        },
        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
        label = { Text(item.title) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = NestNavy,
            selectedTextColor = NestNavy,
            unselectedIconColor = NestTextSecondary,
            unselectedTextColor = NestTextSecondary,
            indicatorColor = NestWhite
        )
    )
}

@Composable
fun NestFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        containerColor = NestGold,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Icon(Icons.Default.Add, contentDescription = "Criar Nova")
    }
}
