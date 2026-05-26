package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.StatusPending
import br.com.gabgrupo.nest.viewmodel.HomeViewModel

@Composable
fun OperatorHomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val userName by viewModel.userName.collectAsState()

    OperatorHomeScreenContent(
        userName = userName,
        onNavigate = onNavigate
    )
}

@Composable
private fun OperatorHomeScreenContent(
    userName: String,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.HOME,
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
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Olá, $userName!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = NestNavy
                    )
                    Text(
                        text = "Que bom te ver por aqui.",
                        fontSize = 14.sp,
                        color = NestTextSecondary
                    )
                }
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notificações",
                    tint = NestNavy,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Jornada de Inovação",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = NestNavy
                )
                Text(
                    text = "Ver todas",
                    fontSize = 12.sp,
                    color = NestTextSecondary,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                JourneyItem(icon = Icons.Default.Lightbulb, title = "Capture", subtitle = "12 ideias", color = NestNavy)
                JourneyItem(icon = Icons.Default.Autorenew, title = "Hatch", subtitle = "5 em análise", color = NestNavy)
                JourneyItem(icon = Icons.Default.FlightTakeoff, title = "Flight", subtitle = "3 em execução", color = NestNavy)
                JourneyItem(icon = Icons.Default.EmojiEvents, title = "Impact", subtitle = "8 resultados", color = StatusPending)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Minhas atividades",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = NestNavy
            )

            Spacer(modifier = Modifier.height(16.dp))

            ActivityCard(
                icon = Icons.Default.Autorenew,
                status = "Ideia em análise",
                title = "Reduzir tempo de check-in",
                subtitle = "há 2 dias",
                progress = null,
                onClick = { }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ActivityCard(
                icon = Icons.Default.FlightTakeoff,
                status = "Projeto em andamento",
                title = "Otimização de roteirização",
                subtitle = "65% concluído",
                progress = 0.65f,
                onClick = { }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Destaques",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = NestNavy
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NestNavy),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Participe, colabore e faça\nparte da transformação.",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { onNavigate("operator/ideas/new") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        modifier = Modifier.background(NestGold, RoundedCornerShape(8.dp))
                    ) {
                        Text(text = "Quero contribuir", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun JourneyItem(icon: ImageVector, title: String, subtitle: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NestNavy)
        Text(text = subtitle, fontSize = 10.sp, color = NestTextSecondary)
    }
}

@Composable
private fun ActivityCard(
    icon: ImageVector,
    status: String,
    title: String,
    subtitle: String,
    progress: Float?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = status,
                tint = NestTextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = status, fontSize = 10.sp, color = NestTextSecondary)
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NestNavy)
                Text(text = subtitle, fontSize = 12.sp, color = NestTextSecondary)
                if (progress != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = NestNavy,
                        trackColor = NestBackground
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Detalhes",
                tint = NestTextSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OperatorHomeScreenPreview() {
    NestTheme {
        OperatorHomeScreenContent(
            userName = "Marcos",
            onNavigate = {}
        )
    }
}