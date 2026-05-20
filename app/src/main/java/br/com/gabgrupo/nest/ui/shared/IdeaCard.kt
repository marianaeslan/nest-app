package br.com.gabgrupo.nest.ui.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite

@Composable
fun IdeaCard(
    title: String,
    status: IdeaStatus,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    categoryIcon: ImageVector = Icons.Default.Lightbulb
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = NestWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lado Esquerdo: Ícone da categoria
            Icon(
                imageVector = categoryIcon,
                contentDescription = "Categoria da ideia",
                tint = NestNavy
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Centro: Título e Badge
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = NestNavy,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatusBadge(status = status)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Lado Direito: Ícone de Favorito
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = if (isFavorite) "Remover dos favoritos" else "Adicionar aos favoritos",
                    tint = NestGold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9FAFB)
@Composable
fun IdeaCardPreview() {
    NestTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            IdeaCard(
                title = "Redução de Desperdício de Papel",
                status = IdeaStatus.PENDING,
                isFavorite = false,
                onFavoriteClick = {},
                onClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            IdeaCard(
                title = "Nova Área de Descanso",
                status = IdeaStatus.APPROVED,
                isFavorite = true,
                onFavoriteClick = {},
                onClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            IdeaCard(
                title = "Melhoria no Refeitório",
                status = IdeaStatus.REJECTED,
                isFavorite = false,
                onFavoriteClick = {},
                onClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            IdeaCard(
                title = "Novo layout do escritório",
                status = IdeaStatus.DRAFT,
                isFavorite = true,
                onFavoriteClick = {},
                onClick = {}
            )
        }
    }
}

