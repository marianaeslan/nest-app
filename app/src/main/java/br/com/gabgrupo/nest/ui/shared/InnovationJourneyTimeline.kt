package br.com.gabgrupo.nest.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gabgrupo.nest.ui.theme.NestBlue
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.StatusApproved
import br.com.gabgrupo.nest.ui.theme.StatusPending

data class TimelinePhase(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun InnovationJourneyTimeline(
    phases: List<TimelinePhase>,
    modifier: Modifier = Modifier
) {
    val lineColor = NestTextSecondary.copy(alpha = 0.2f)
    val circleSize = 40.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val circleSizePx = circleSize.toPx()
                val verticalCenter = circleSizePx / 2
                
                // Draw lines between nodes
                // We assume 4 nodes as requested, evenly spaced
                val nodeCount = phases.size
                if (nodeCount > 1) {
                    val sectionWidth = size.width / nodeCount
                    for (i in 0 until nodeCount - 1) {
                        val startX = (sectionWidth * i) + (sectionWidth / 2)
                        val endX = (sectionWidth * (i + 1)) + (sectionWidth / 2)
                        
                        drawLine(
                            color = lineColor,
                            start = Offset(startX, verticalCenter),
                            end = Offset(endX, verticalCenter),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        phases.forEach { phase ->
            TimelineNode(
                phase = phase,
                circleSize = circleSize,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TimelineNode(
    phase: TimelinePhase,
    circleSize: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(phase.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = phase.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = phase.title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = NestNavy
        )

        Text(
            text = phase.subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = NestTextSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InnovationJourneyTimelinePreview() {
    val phases = listOf(
        TimelinePhase("Capture", "12 ideias", Icons.Default.Lightbulb, NestBlue),
        TimelinePhase("Hatch", "5 ideias", Icons.Default.Add, StatusPending),
        TimelinePhase("Fly", "3 ideias", Icons.Default.PlayArrow, NestNavy),
        TimelinePhase("Land", "1 ideia", Icons.Default.Check, StatusApproved)
    )

    NestTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            InnovationJourneyTimeline(phases = phases)
        }
    }
}
