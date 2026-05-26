package br.com.gabgrupo.nest.ui.leader

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.ProjectSummary
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.shared.SectionHeader
import br.com.gabgrupo.nest.ui.theme.*
import br.com.gabgrupo.nest.viewmodel.LeaderViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LeaderHomeScreen(viewModel: LeaderViewModel = hiltViewModel()) {
    var currentNav by remember { mutableStateOf(NavItem.PROJECTS) }
    val userName by viewModel.userName.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val dashboard by viewModel.dashboard.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            NestTopAppBar(
                title = {
                    Text(
                        text = "Impact",
                        fontWeight = FontWeight.Bold,
                        color = NestNavy,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    if (error != null) {
                        IconButton(onClick = { viewModel.loadDashboard() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Tentar novamente")
                        }
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificações")
                    }
                }
            )
        },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = currentNav,
                userRole = userRole,
                onNavigate = { route ->
                    currentNav = when (route) {
                        "leader/dashboard" -> NavItem.HOME
                        "leader/projects" -> NavItem.PROJECTS
                        "leader/users" -> NavItem.USERS
                        "profile" -> NavItem.PROFILE
                        else -> NavItem.HOME
                    }
                },
                onFabClick = {}
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NestGold)
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Erro ao carregar dashboard",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadDashboard() },
                            colors = ButtonDefaults.buttonColors(containerColor = NestGold)
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NestBackground)
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // KPI Cards
                    item {
                        dashboard?.let { data ->
                            KpiGrid(
                                roi = data.totalRoi,
                                savings = data.totalSavings,
                                completedProjects = data.completedProjects,
                                ideasImplemented = data.ideasImplemented
                            )
                        } ?: KpiGridSkeleton()
                    }

                    // Gráfico de Rosca — Impacto por Categoria
                    item {
                        SectionHeader(
                            title = "Impacto por categoria",
                            actionText = "",
                            onActionClick = {}
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ImpactDonutChart()
                    }

                    // Gráfico de Linha — Evolução do ROI
                    item {
                        SectionHeader(
                            title = "Evolução do ROI",
                            actionText = "",
                            onActionClick = {}
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        RoiLineChart(targetRoi = dashboard?.totalRoi ?: 245.0)
                    }

                    // Projetos
                    item {
                        SectionHeader(
                            title = "Projetos em Andamento",
                            actionText = "Ver todos",
                            onActionClick = {}
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    dashboard?.projects?.let { projects ->
                        items(projects.filter { it.status == "IN_PROGRESS" }) { project ->
                            ProjectSummaryCard(project = project)
                        }
                    }
                }
            }
        }
    }
}

// ── KPI Grid ──────────────────────────────────────────────────────────────────

@Composable
fun KpiGrid(
    roi: Double,
    savings: Double,
    completedProjects: Long,
    ideasImplemented: Long
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard(
                modifier = Modifier.weight(1f),
                label = "ROI Total",
                value = "${roi.toInt()}%",
                valueColor = androidx.compose.ui.graphics.Color(0xFF10B981)
            )
            KpiCard(
                modifier = Modifier.weight(1f),
                label = "Economia gerada",
                value = currencyFormat.format(savings).replace("BRL", "R$")
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard(
                modifier = Modifier.weight(1f),
                label = "Projetos concluídos",
                value = completedProjects.toString()
            )
            KpiCard(
                modifier = Modifier.weight(1f),
                label = "Ideias implementadas",
                value = ideasImplemented.toString()
            )
        }
    }
}

@Composable
fun KpiCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = NestNavy
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = androidx.compose.ui.graphics.Color(0xFF6B7280)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                fontSize = 22.sp
            )
        }
    }
}

@Composable
fun KpiGridSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(androidx.compose.ui.graphics.Color(0xFFE5E7EB))
                    )
                }
            }
        }
    }
}

// ── Donut Chart ───────────────────────────────────────────────────────────────

@Composable
fun ImpactDonutChart() {
    val categories = listOf(
        "Operações" to 45f,
        "Exp. cliente" to 25f,
        "Tecnologia" to 20f,
        "Outros" to 10f
    )
    val colors = listOf(
        Color.parseColor("#0D1B3E"),
        Color.parseColor("#10B981"),
        Color.parseColor("#3B82F6"),
        Color.parseColor("#C89B3C")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AndroidView(
                modifier = Modifier.size(140.dp),
                factory = { context ->
                    PieChart(context).apply {
                        val entries = categories.map { PieEntry(it.second) }
                        val dataSet = PieDataSet(entries, "").apply {
                            this.colors = colors
                            sliceSpace = 2f
                            setDrawValues(false)
                        }
                        data = PieData(dataSet)
                        description.isEnabled = false
                        legend.isEnabled = false
                        isDrawHoleEnabled = true
                        holeRadius = 55f
                        transparentCircleRadius = 60f
                        setHoleColor(Color.WHITE)
                        setHoleColor(Color.WHITE)
                        setDrawCenterText(false)
                        setTouchEnabled(false)
                        animateY(800)
                    }
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEachIndexed { index, (label, value) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(
                                    androidx.compose.ui.graphics.Color(colors[index].toLong() + 0xFF000000)
                                )
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${value.toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = NestNavy
                        )
                    }
                }
            }
        }
    }
}

// ── Line Chart ────────────────────────────────────────────────────────────────

@Composable
fun RoiLineChart(targetRoi: Double) {
    val months = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun")

    val percentages = listOf(0.05, 0.15, 0.35, 0.55, 0.75, 1.0)
    val values = percentages.map { pct ->
        (targetRoi.coerceAtLeast(0.0) * pct).toFloat()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(8.dp),
            factory = { context ->
                LineChart(context).apply {
                    val entries = values.mapIndexed { i, v -> Entry(i.toFloat(), v) }
                    val dataSet = LineDataSet(entries, "ROI %").apply {
                        color = Color.parseColor("#0D1B3E")
                        setCircleColor(Color.parseColor("#0D1B3E"))
                        lineWidth = 2f
                        circleRadius = 4f
                        setDrawCircleHole(true)
                        circleHoleRadius = 2f
                        setDrawValues(false)
                        setDrawFilled(true)
                        fillColor = Color.parseColor("#0D1B3E")
                        fillAlpha = 20
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                    }
                    data = LineData(dataSet)
                    description.isEnabled = false
                    legend.isEnabled = false
                    setTouchEnabled(false)
                    axisRight.isEnabled = false
                    axisLeft.apply {
                        textSize = 10f
                        textColor = Color.parseColor("#6B7280")
                        setDrawGridLines(true)
                        gridColor = Color.parseColor("#F3F4F6")
                        axisMinimum = 0f
                        spaceTop = 20f
                    }
                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        textSize = 10f
                        textColor = Color.parseColor("#6B7280")
                        setDrawGridLines(false)
                        valueFormatter = IndexAxisValueFormatter(months)
                        granularity = 1f
                    }
                    animateX(1000)
                }
            }
        )
    }
}

// ── Project Summary Card ──────────────────────────────────────────────────────

@Composable
fun ProjectSummaryCard(project: ProjectSummary) {
    val stageLabel = when (project.stage) {
        "IDEATION" -> "Ideação"
        "VALIDATION" -> "Validação"
        "PLANNING" -> "Planejamento"
        "EXECUTION" -> "Execução"
        "MONITORING" -> "Monitoramento"
        "COMPLETED" -> "Concluído"
        else -> project.stage
    }
    val progress = when (project.stage) {
        "IDEATION" -> 0.1f
        "VALIDATION" -> 0.25f
        "PLANNING" -> 0.4f
        "EXECUTION" -> 0.65f
        "MONITORING" -> 0.85f
        "COMPLETED" -> 1f
        else -> 0f
    }
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NestNavy,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = NestNavy
                )
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = NestNavy,
                trackColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Etapa",
                        style = MaterialTheme.typography.labelSmall,
                        color = androidx.compose.ui.graphics.Color(0xFF6B7280)
                    )
                    Text(
                        text = stageLabel,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = NestNavy
                    )
                }
                project.investment?.let {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Investimento",
                            style = MaterialTheme.typography.labelSmall,
                            color = androidx.compose.ui.graphics.Color(0xFF6B7280)
                        )
                        Text(
                            text = currencyFormat.format(it).replace("BRL", "R$"),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = NestNavy
                        )
                    }
                }
            }
        }
    }
}
