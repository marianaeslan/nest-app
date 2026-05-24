package br.com.gabgrupo.nest.ui.screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.IdeaRequest
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.ui.theme.StatusApproved
import br.com.gabgrupo.nest.viewmodel.IdeaActionState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel

@Composable
fun CreateIdeaScreen(
    viewModel: IdeaViewModel = hiltViewModel(),
    onNavigateToHatch: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val actionState by viewModel.actionState.collectAsState()

    LaunchedEffect(actionState) {
        if (actionState is IdeaActionState.Success) {
            viewModel.resetActionState()
            onNavigateToHatch()
        }
    }

    CreateIdeaScreenContent(
        actionState = actionState,
        onNavigateBack = onNavigateBack,
        onSubmit = { title, description ->
            viewModel.createIdea(IdeaRequest(title, description))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateIdeaScreenContent(
    actionState: IdeaActionState,
    onNavigateBack: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(1) }

    var title by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var impact by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova ideia", fontWeight = FontWeight.Bold, color = NestNavy) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 1) currentStep-- else onNavigateBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = NestNavy)
                    }
                },
                actions = {
                    Text(
                        text = "Salvar rascunho",
                        color = NestGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { onNavigateBack() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NestWhite)
            )
        },
        containerColor = NestBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FormStepper(currentStep = currentStep)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                when (currentStep) {
                    1 -> {
                        Text(
                            text = "Qual problema você quer resolver?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NestNavy
                        )
                        Text(
                            text = "Conte-nos sobre a dor ou oportunidade que você identificou.",
                            fontSize = 14.sp,
                            color = NestTextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                        )

                        Text("Título da ideia", fontWeight = FontWeight.Medium, color = NestNavy, fontSize = 14.sp)
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("Ex: Reduzir tempo de embarque") },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NestGold,
                                focusedContainerColor = NestWhite,
                                unfocusedContainerColor = NestWhite
                            ),
                            singleLine = true
                        )

                        Text("Qual é o problema?", fontWeight = FontWeight.Medium, color = NestNavy, fontSize = 14.sp)
                        OutlinedTextField(
                            value = problem,
                            onValueChange = { if (it.length <= 500) problem = it },
                            placeholder = { Text("Descreva a dor ou oportunidade...") },
                            modifier = Modifier.fillMaxWidth().height(120.dp).padding(top = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NestGold,
                                focusedContainerColor = NestWhite,
                                unfocusedContainerColor = NestWhite
                            ),
                            supportingText = {
                                Text(
                                    text = "${problem.length}/500",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                        )

                        Text("Onde acontece?", fontWeight = FontWeight.Medium, color = NestNavy, fontSize = 14.sp)
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            placeholder = { Text("Ex: Terminal Tietê") },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NestGold,
                                focusedContainerColor = NestWhite,
                                unfocusedContainerColor = NestWhite
                            ),
                            singleLine = true
                        )

                        Text("Impacto observado", fontWeight = FontWeight.Medium, color = NestNavy, fontSize = 14.sp)
                        OutlinedTextField(
                            value = impact,
                            onValueChange = { if (it.length <= 300) impact = it },
                            placeholder = { Text("Qual o impacto desse problema?") },
                            modifier = Modifier.fillMaxWidth().height(100.dp).padding(top = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NestGold,
                                focusedContainerColor = NestWhite,
                                unfocusedContainerColor = NestWhite
                            ),
                            supportingText = {
                                Text(
                                    text = "${impact.length}/300",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                        )

                        Text("Adicione fotos ou arquivos (opcional)", fontWeight = FontWeight.Medium, color = NestNavy, fontSize = 14.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 32.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NestWhite)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = "Foto", tint = NestTextSecondary)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NestWhite)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.InsertDriveFile, contentDescription = "Arquivo", tint = NestTextSecondary)
                            }
                        }

                        Button(
                            onClick = { if (title.isNotBlank() && problem.isNotBlank()) currentStep = 2 },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NestNavy, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            enabled = title.isNotBlank() && problem.isNotBlank()
                        ) {
                            Text("Continuar", fontWeight = FontWeight.Bold)
                        }
                    }

                    2 -> {
                        Text(
                            text = "Revisar Detalhes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NestNavy
                        )
                        Text(
                            text = "Confira as informações antes de enviar para o ninho.",
                            fontSize = 14.sp,
                            color = NestTextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                        )

                        ReviewItem(label = "Título", value = title)
                        ReviewItem(label = "Problema", value = problem)
                        ReviewItem(label = "Local de ocorrência", value = location)
                        ReviewItem(label = "Impacto", value = impact)

                        Spacer(modifier = Modifier.height(40.dp))

                        Button(
                            onClick = { currentStep = 3 },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NestNavy, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Confirmar Detalhes", fontWeight = FontWeight.Bold)
                        }
                    }

                    3 -> {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier.size(80.dp).clip(CircleShape).background(StatusApproved.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Check, contentDescription = "Sucesso", tint = StatusApproved, modifier = Modifier.size(40.dp))
                            }

                            Text(
                                text = "Pronto para enviar!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = NestNavy,
                                modifier = Modifier.padding(top = 24.dp)
                            )

                            Text(
                                text = "Sua ideia será enviada com segurança para análise dos gestores.",
                                fontSize = 14.sp,
                                color = NestTextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                            )

                            if (actionState is IdeaActionState.Error) {
                                Text(
                                    text = actionState.message,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(40.dp))

                            Button(
                                onClick = { onSubmit(title, problem) },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NestGold, contentColor = Color.White),
                                shape = RoundedCornerShape(8.dp),
                                enabled = actionState !is IdeaActionState.Loading
                            ) {
                                if (actionState is IdeaActionState.Loading) {
                                    CircularProgressIndicator(color = NestWhite, modifier = Modifier.size(24.dp))
                                } else {
                                    Text("ENVIAR IDEIA", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormStepper(currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NestWhite)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StepperItem(stepNumber = 1, label = "Capture", isActive = currentStep >= 1)
        HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 8.dp), thickness = 2.dp, color = if (currentStep >= 2) NestNavy else Color.LightGray)
        StepperItem(stepNumber = 2, label = "Detalhes", isActive = currentStep >= 2)
        HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 8.dp), thickness = 2.dp, color = if (currentStep >= 3) NestNavy else Color.LightGray)
        StepperItem(stepNumber = 3, label = "Conclusão", isActive = currentStep >= 3)
    }
}

@Composable
private fun StepperItem(stepNumber: Int, label: String, isActive: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isActive) NestNavy else Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stepNumber.toString(), color = NestWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, color = if (isActive) NestNavy else NestTextSecondary, fontSize = 12.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun ReviewItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(text = label, fontSize = 12.sp, color = NestTextSecondary, fontWeight = FontWeight.Medium)
        Text(text = value.ifBlank { "Não informado" }, fontSize = 15.sp, color = NestNavy, fontWeight = FontWeight.Normal, modifier = Modifier.padding(top = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateIdeaScreenStep1Preview() {
    NestTheme {
        CreateIdeaScreenContent(
            actionState = IdeaActionState.Idle,
            onNavigateBack = {},
            onSubmit = { _, _ -> }
        )
    }
}