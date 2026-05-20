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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme

@Composable
fun FormStepper(
    steps: List<String>,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    val circleSize = 32.dp

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Linha divisória que conecta os passos
        Divider(
            modifier = Modifier
                .fillMaxWidth(0.8f) // Não ocupa a largura toda para não tocar nas bordas
                .align(Alignment.TopCenter)
                .padding(top = circleSize / 2)
                .zIndex(0f),
            color = NestTextSecondary.copy(alpha = 0.3f),
            thickness = 1.dp
        )

        // Row para os passos, que ficará sobre a linha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f), // Garante que a Row fique na frente da Divider
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            steps.forEachIndexed { index, title ->
                Step(
                    number = index + 1,
                    title = title,
                    isActive = index == currentStep,
                    circleSize = circleSize
                )
            }
        }
    }
}

@Composable
private fun Step(
    number: Int,
    title: String,
    isActive: Boolean,
    circleSize: androidx.compose.ui.unit.Dp
) {
    val circleColor = if (isActive) NestNavy else NestTextSecondary.copy(alpha = 0.1f)
    val numberColor = if (isActive) Color.White else NestTextSecondary
    val titleColor = if (isActive) NestNavy else NestTextSecondary
    val titleWeight = if (isActive) FontWeight.Bold else FontWeight.Normal

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Círculo com o número
        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(circleColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = numberColor,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Rótulo do passo
        Text(
            text = title,
            color = titleColor,
            fontWeight = titleWeight,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormStepperPreview() {
    var currentStep by remember { mutableStateOf(1) }
    val steps = listOf("Capture", "Detalhes", "Conclusão")

    NestTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            FormStepper(
                steps = steps,
                currentStep = currentStep
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // Botões apenas para simular a troca de passos no preview
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NestButton(text = "Anterior", onClick = { if (currentStep > 0) currentStep-- })
                NestButton(text = "Próximo", onClick = { if (currentStep < steps.size - 1) currentStep++ })
            }
        }
    }
}
