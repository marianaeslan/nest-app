package br.com.gabgrupo.nest.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.gabgrupo.nest.ui.theme.StatusApproved
import br.com.gabgrupo.nest.ui.theme.StatusDraft
import br.com.gabgrupo.nest.ui.theme.StatusPending
import br.com.gabgrupo.nest.ui.theme.StatusRejected
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import br.com.gabgrupo.nest.ui.theme.NestTheme

enum class IdeaStatus(val label: String) {
    PENDING("Em análise"),
    APPROVED("Aprovada"),
    REJECTED("Rejeitada"),
    DRAFT("Rascunho")
}

@Composable
fun StatusBadge(
    status: IdeaStatus,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (status) {
        IdeaStatus.PENDING -> StatusPending
        IdeaStatus.APPROVED -> StatusApproved
        IdeaStatus.REJECTED -> StatusRejected
        IdeaStatus.DRAFT -> StatusDraft
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.label,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatusBadgePreview() {
    NestTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatusBadge(status = IdeaStatus.PENDING)
            StatusBadge(status = IdeaStatus.APPROVED)
            StatusBadge(status = IdeaStatus.REJECTED)
            StatusBadge(status = IdeaStatus.DRAFT)
        }
    }
}
