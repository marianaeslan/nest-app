package br.com.gabgrupo.nest.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite

/**
 * TopAppBar customizado flexível recebendo um composable direto para o title.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NestWhite,
            titleContentColor = NestNavy,
            navigationIconContentColor = NestNavy,
            actionIconContentColor = NestNavy
        )
    )
}

/**
 * TopAppBar para uso simples passando apenas String no título.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    NestTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions
    )
}

@Preview(showBackground = true)
@Composable
fun NestTopAppBarPreview() {
    NestTheme {
        Column {
            // Exemplo 1: Título simples ("Ideias")
            NestTopAppBar(
                title = "Ideias",
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificações")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exemplo 2: Com navigation icon (voltar) e ação em texto
            NestTopAppBar(
                title = "Nova ideia",
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    TextButton(onClick = {}) {
                        Text("Salvar rascunho", color = NestGold, fontWeight = FontWeight.Bold)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exemplo 3: Título customizado (Saudação)
            NestTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Olá, Marcos!",
                            fontWeight = FontWeight.Bold,
                            color = NestNavy,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Bem-vindo de volta",
                            color = NestTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificações")
                    }
                }
            )
        }
    }
}

