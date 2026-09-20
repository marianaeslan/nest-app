package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.R
import br.com.gabgrupo.nest.data.model.IdeaResponse
import br.com.gabgrupo.nest.data.model.IdeaStatus
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.data.model.UserSummary
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.viewmodel.HomeViewModel
import br.com.gabgrupo.nest.viewmodel.IdeaListState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel

@Composable
fun OperatorHomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    ideaViewModel: IdeaViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val userName by viewModel.userName.collectAsState()
    val ideaState by ideaViewModel.listState.collectAsState()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        ideaViewModel.getMyIdeas()
    }

    OperatorHomeScreenContent(
        userName = userName,
        ideas = (ideaState as? IdeaListState.Success)?.ideas.orEmpty(),
        isLoadingIdeas = ideaState is IdeaListState.Loading,
        ideasError = (ideaState as? IdeaListState.Error)?.message,
        onNavigate = onNavigate
    )
}

@Composable
private fun OperatorHomeScreenContent(
    userName: String,
    ideas: List<IdeaResponse>,
    isLoadingIdeas: Boolean,
    ideasError: String?,
    onNavigate: (String) -> Unit
) {
    var selectedIdea by remember { mutableStateOf<IdeaResponse?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.HOME,
                userRole = UserRole.OPERATOR,
                onNavigate = onNavigate,
                onFabClick = {
                    onNavigate("operator/ideas/new")
                }
            )
        },
        containerColor = NestBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                )
        ) {

            // ============================================================
            // CABEÇALHO
            // ============================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Column {

                    Text(
                        text = "Olá, $userName! 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = NestNavy
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
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
                    modifier = Modifier.size(27.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // ============================================================
            // JORNADA DE INOVAÇÃO
            // ============================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Jornada de Inovação",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = NestNavy
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            InnovationJourney(
                ideasCount = ideas.size,
                pendingCount = ideas.count {
                    it.status == IdeaStatus.PENDING
                },
                approvedCount = ideas.count {
                    it.status == IdeaStatus.APPROVED
                },
                rejectedCount = ideas.count {
                    it.status == IdeaStatus.REJECTED
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // ============================================================
            // MINHAS ATIVIDADES
            // ============================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Minhas atividades",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = NestNavy
                )
                if (ideas.isNotEmpty()) {
                    Text(
                        text = "Ver minhas ideias",
                        fontSize = 13.sp,
                        color = NestTextSecondary,
                        modifier = Modifier.clickable {
                            onNavigate("operator/ideas")
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            when {

                isLoadingIdeas -> {
                    Text(
                        text = "Carregando suas ideias...",
                        color = NestTextSecondary
                    )
                }

                ideasError != null -> {
                    Text(
                        text = ideasError,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                ideas.isEmpty() -> {
                    Text(
                        text = "Você ainda não cadastrou ideias.",
                        color = NestTextSecondary
                    )
                }

                else -> {
                    ideas
                        .sortedByDescending {
                            it.createdAt.orEmpty()
                        }
                        .take(2)
                        .forEach { idea ->

                            ActivityCard(
                                icon = if (
                                    idea.status == IdeaStatus.APPROVED
                                ) {
                                    Icons.Default.FlightTakeoff
                                } else {
                                    Icons.Default.Autorenew
                                },
                                status = idea.status.label, // Usando o label padrão
                                title = idea.title,
                                subtitle = idea.guidelineId?.let {
                                    "Guideline vinculada"
                                } ?: "Sem guideline vinculada",
                                progress = null,
                                statusColor = when (idea.status) {
                                    IdeaStatus.PENDING -> Color(0xFFB7791F)
                                    IdeaStatus.PRIORITIZED -> NestGold
                                    IdeaStatus.APPROVED -> Color(0xFF16825D)
                                    IdeaStatus.REJECTED -> Color(0xFFB3261E)
                                },
                                onClick = {
                                    if (idea.status == IdeaStatus.PENDING) {
                                        selectedIdea = idea
                                    }
                                }
                            )

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )
                        }
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // ============================================================
            // DESTAQUES
            // ============================================================

            Text(
                text = "Destaques",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = NestNavy
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            HighlightCard(
                onClick = {
                    onNavigate("operator/ideas/new")
                }
            )

            Spacer(
                modifier = Modifier.height(90.dp)
            )
        }
    }

    selectedIdea?.let { idea ->
        IdeaDetailsDialog(
            idea = idea,
            onDismiss = { selectedIdea = null }
        )
    }
}

@Composable
private fun IdeaDetailsDialog(
    idea: IdeaResponse,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalhes da Ideia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NestNavy
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                DetailItem(label = "Título", value = idea.title)
                DetailItem(label = "Descrição", value = idea.description)
                DetailItem(
                    label = "Status",
                    value = idea.status.label, // Substituído o when pelo label padrão do enum
                    valueColor = when (idea.status) {
                        IdeaStatus.PENDING -> Color(0xFFB7791F)
                        IdeaStatus.PRIORITIZED -> NestGold
                        IdeaStatus.APPROVED -> Color(0xFF16825D)
                        IdeaStatus.REJECTED -> Color(0xFFB3261E)
                    }
                )
                DetailItem(
                    label = "Guideline",
                    value = idea.guidelineId ?: "Nenhuma selecionada"
                )
                DetailItem(
                    label = "Prioridade",
                    value = idea.priority?.toString() ?: "Não definida"
                )
                DetailItem(
                    label = "Autor",
                    value = idea.submittedBy.name
                )
                DetailItem(
                    label = "Data de submissão",
                    value = idea.createdAt?.substringBefore("T") ?: "Não disponível"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NestNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Fechar")
                }
            }
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String,
    valueColor: Color = NestNavy
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = NestTextSecondary,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 15.sp,
            color = valueColor,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFF1F1F1))
    }
}

@Composable
private fun InnovationJourney(
    ideasCount: Int,
    pendingCount: Int,
    approvedCount: Int,
    rejectedCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {

        JourneyItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Lightbulb,
            title = "Capture",
            subtitle = "$ideasCount ideias",
            color = NestNavy
        )

        JourneyConnector(
            modifier = Modifier.weight(0.35f)
        )

        JourneyItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Autorenew,
            title = "Hatch",
            subtitle = "$pendingCount em análise",
            color = NestGold
        )

        JourneyConnector(
            modifier = Modifier.weight(0.35f)
        )

        JourneyItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.FlightTakeoff,
            title = "Flight",
            subtitle = "$approvedCount em execução",
            color = NestNavy
        )

        JourneyConnector(
            modifier = Modifier.weight(0.35f)
        )

        JourneyItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.EmojiEvents,
            title = "Impact",
            subtitle = "$rejectedCount resultados",
            color = Color(0xFF16825D)
        )
    }
}

@Composable
private fun JourneyItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NestNavy
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = subtitle,
            fontSize = 10.sp,
            color = NestTextSecondary,
            maxLines = 1
        )
    }
}

@Composable
private fun JourneyConnector(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 25.dp)
            .height(2.dp)
            .background(NestTextSecondary.copy(alpha = 0.45f))
    )
}

@Composable
private fun ActivityCard(
    icon: ImageVector,
    status: String,
    title: String,
    subtitle: String,
    progress: Float?,
    statusColor: Color = NestTextSecondary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .border(
                width = 1.dp,
                color = Color(0xFFE9E9E9),
                shape = RoundedCornerShape(14.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        shape = RoundedCornerShape(14.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 15.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        NestBackground
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = status,
                    tint = NestNavy,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = status,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = statusColor
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NestNavy
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = NestTextSecondary
                )

                if (progress != null) {

                    Spacer(
                        modifier = Modifier.height(9.dp)
                    )

                    LinearProgressIndicator(
                        progress = {
                            progress
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(
                                RoundedCornerShape(4.dp)
                            ),
                        color = NestNavy,
                        trackColor = NestBackground
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            // Agora o ícone só aparece se for PENDING, indicando que é clicável para ver detalhes
            if (status == IdeaStatus.PENDING.label) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Detalhes",
                    tint = NestTextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun HighlightCard(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(NestNavy)
            .clickable {
                onClick()
            }
    ) {

        Column(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = 22.dp,
                    end = 120.dp
                )
        ) {

            Text(
                text = "Participe, colabore e faça\nparte da transformação.",
                color = Color.White,
                fontSize = 17.sp,
                lineHeight = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(40.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = "Quero contribuir",
                    fontSize = 12.sp
                )
            }
        }

        Image(
            painter = painterResource(
                id = R.drawable.lamp
            ),
            contentDescription = "Ideia",
            modifier = Modifier
                .size(125.dp)
                .align(Alignment.BottomEnd)
                .offset(
                    x = (-6).dp,
                    y = 4.dp
                ),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true, name = "Success State")
@Composable
fun OperatorHomeScreenPreview() {
    val mockUser = UserSummary("1", "Lucas", UserRole.OPERATOR)
    val mockIdeas = listOf(
        IdeaResponse(
            id = "1",
            title = "Otimização de Processo A",
            description = "Descrição detalhada da ideia A",
            status = IdeaStatus.PENDING,
            priority = null,
            submittedBy = mockUser,
            reviewedBy = null,
            createdAt = "2023-10-27T10:00:00Z"
        ),
        IdeaResponse(
            id = "2",
            title = "Redução de Desperdício B",
            description = "Descrição detalhada da ideia B",
            status = IdeaStatus.APPROVED,
            priority = 2,
            submittedBy = mockUser,
            reviewedBy = mockUser,
            createdAt = "2023-10-26T14:30:00Z"
        ),
        IdeaResponse(
            id = "3",
            title = "Melhoria Ergonômica C",
            description = "Descrição detalhada da ideia C",
            status = IdeaStatus.REJECTED,
            priority = null,
            submittedBy = mockUser,
            reviewedBy = mockUser,
            createdAt = "2023-10-25T09:15:00Z"
        )
    )

    NestTheme {
        OperatorHomeScreenContent(
            userName = "Lucas",
            ideas = mockIdeas,
            isLoadingIdeas = false,
            ideasError = null,
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
fun OperatorHomeScreenEmptyPreview() {
    NestTheme {
        OperatorHomeScreenContent(
            userName = "Lucas",
            ideas = emptyList(),
            isLoadingIdeas = false,
            ideasError = null,
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun OperatorHomeScreenLoadingPreview() {
    NestTheme {
        OperatorHomeScreenContent(
            userName = "Lucas",
            ideas = emptyList(),
            isLoadingIdeas = true,
            ideasError = null,
            onNavigate = {}
        )
    }
}
