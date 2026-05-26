package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.ui.theme.StatusApproved

@Composable
fun HatchScreen(
    onNavigate: (String) -> Unit
) {
    HatchScreenContent(
        ideaTitle = "Reduzir tempo de check-in dos passageiros",
        currentStep = 2,
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HatchScreenContent(
    ideaTitle: String,
    currentStep: Int,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hatch", fontWeight = FontWeight.Bold, color = NestNavy) },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opções", tint = NestNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NestWhite)
            )
        },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.IDEAS,
                userRole = UserRole.OPERATOR,
                onNavigate = onNavigate,
                onFabClick = { onNavigate("operator/ideas/new") }
            )
        },
        containerColor = NestBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(NestWhite, NestBackground)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(NestGold.copy(alpha = 0.1f))
                            .border(2.dp, NestGold.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "🥚",
                            fontSize = 54.sp,
                            modifier = Modifier.offset(y = (-6).dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ideia em análise",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NestNavy
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Sua ideia está sendo avaliada pelos nossos gestores. Em breve teremos novidades!",
                        fontSize = 13.sp,
                        color = NestTextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = CardDefaults.cardColors(containerColor = NestWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = ideaTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = NestNavy,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    TimelineItem(
                        title = "Recebida",
                        subtitle = "há 2 dias",
                        isCompleted = currentStep > 1,
                        isActive = currentStep == 1,
                        isLast = false
                    )
                    TimelineItem(
                        title = "Em análise",
                        subtitle = "há 1 dia",
                        isCompleted = currentStep > 2,
                        isActive = currentStep == 2,
                        isLast = false
                    )
                    TimelineItem(
                        title = "Em validação",
                        subtitle = null,
                        isCompleted = currentStep > 3,
                        isActive = currentStep == 3,
                        isLast = false
                    )
                    TimelineItem(
                        title = "Decisão final",
                        subtitle = null,
                        isCompleted = currentStep > 4,
                        isActive = currentStep == 4,
                        isLast = true
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 80.dp),
                colors = CardDefaults.cardColors(containerColor = NestGold.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NestGold.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Aviso",
                        tint = NestGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Fique de olho! Você será notificado sobre qualquer atualização.",
                        fontSize = 12.sp,
                        color = NestNavy,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineItem(
    title: String,
    subtitle: String?,
    isCompleted: Boolean,
    isActive: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> StatusApproved
                            isActive -> NestNavy
                            else -> Color.LightGray.copy(alpha = 0.6f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Concluído",
                        tint = NestWhite,
                        modifier = Modifier.size(14.dp)
                    )
                } else if (isActive) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(NestGold)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(if (isCompleted) StatusApproved else Color.LightGray.copy(alpha = 0.6f))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                color = if (isActive || isCompleted) NestNavy else NestTextSecondary
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = NestTextSecondary,

                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HatchScreenPreview() {
    NestTheme {
        HatchScreenContent(
            ideaTitle = "Melhoria no processo de limpeza dos ônibus",
            currentStep = 2,
            onNavigate = {}
        )
    }
}