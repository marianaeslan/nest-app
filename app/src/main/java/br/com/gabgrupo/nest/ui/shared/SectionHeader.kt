package br.com.gabgrupo.nest.ui.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = NestNavy,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        if (actionText != null) {
            Text(
                text = actionText,
                color = NestTextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { 
                    if (onActionClick != null) onActionClick() 
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    NestTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Exemplo com Action Text (Ver todas)
            SectionHeader(
                title = "Jornada de Inovação",
                actionText = "Ver todas",
                onActionClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exemplo sem Action Text
            SectionHeader(
                title = "Minhas atividades"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exemplo Destaques
            SectionHeader(
                title = "Destaques",
                actionText = "Ver detalhes",
                onActionClick = {}
            )
        }
    }
}

