package br.com.gabgrupo.nest.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite

enum class NavItem(val title: String, val icon: ImageVector) {
    HOME("Início", Icons.Default.Home),
    IDEAS("Ideias", Icons.Default.Lightbulb),
    PROJECTS("Projetos", Icons.Default.Folder),
    PROFILE("Perfil", Icons.Default.Person)
}

@Composable
fun NestBottomNavBar(
    currentRoute: NavItem,
    onNavigate: (NavItem) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Correção: O Box agora ocupa a largura máxima com fillMaxWidth()
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxWidth()) {
        NavigationBar(
            containerColor = NestWhite,
            contentColor = NestTextSecondary
        ) {
            val items = NavItem.entries.toTypedArray()

            // As 2 primeiras ações
            items.take(2).forEach { item ->
                NestNavItem(item, currentRoute == item, onNavigate)
            }

            // Espaço central vazio reservado para o FAB
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { },
                label = { },
                enabled = false
            )

            // As 2 últimas ações
            items.drop(2).forEach { item ->
                NestNavItem(item, currentRoute == item, onNavigate)
            }
        }

        // O FAB centralizado e sobreposto à barra
        NestFab(
            onClick = onFabClick,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun RowScope.NestNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: (NavItem) -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = { onClick(item) },
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

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    var currentItem by remember { mutableStateOf(NavItem.HOME) }

    NestTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NestBottomNavBar(
                    currentRoute = currentItem,
                    onNavigate = { currentItem = it },
                    onFabClick = {}
                )
            }
        ) { innerPadding ->

            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                Text(
                    text = "Conteúdo Principal",
                    modifier = Modifier.padding(16.dp),
                    color = NestNavy
                )
            }
        }
    }
}