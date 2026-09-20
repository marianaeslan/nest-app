package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.data.model.GuidelineResponse
import br.com.gabgrupo.nest.viewmodel.IdeaActionState
import br.com.gabgrupo.nest.viewmodel.IdeaViewModel
import br.com.gabgrupo.nest.viewmodel.GuidelineListState
import br.com.gabgrupo.nest.viewmodel.GuidelineViewModel

@Composable
fun CreateIdeaScreen(
    viewModel: IdeaViewModel = hiltViewModel(),
    guidelineViewModel: GuidelineViewModel = hiltViewModel(),
    onNavigateToHatch: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val actionState by viewModel.actionState.collectAsState()
    val guidelineState by guidelineViewModel.listState.collectAsState()

    LaunchedEffect(Unit) {
        guidelineViewModel.getAllGuidelines()
    }

    LaunchedEffect(actionState) {
        if (actionState is IdeaActionState.Success) {
            viewModel.resetActionState()
            onNavigateToHatch()
        }
    }

    CreateIdeaScreenContent(
        actionState = actionState,
        guidelines = (guidelineState as? GuidelineListState.Success)?.guidelines.orEmpty(),
        guidelinesError = (guidelineState as? GuidelineListState.Error)?.message,
        onNavigateBack = onNavigateBack,
        onSubmit = { title, description, guidelineId ->
            viewModel.createIdea(br.com.gabgrupo.nest.data.model.IdeaRequest(title, description, guidelineId))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateIdeaScreenContent(
    actionState: IdeaActionState,
    guidelines: List<GuidelineResponse> = emptyList(),
    guidelinesError: String? = null,
    onNavigateBack: () -> Unit,
    onSubmit: (String, String, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedGuidelineId by remember { mutableStateOf<String?>(null) }
    var selectedGuidelineTitle by remember { mutableStateOf("Sem guideline") }
    var guidelineMenuExpanded by remember { mutableStateOf(false) }

    val isLoading = actionState is IdeaActionState.Loading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Ideia", fontWeight = FontWeight.Bold, color = NestNavy) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = NestNavy)
                    }
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Qual problema você quer resolver?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NestNavy
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Descreva o problema e a solução proposta para que possamos analisar.",
                fontSize = 14.sp,
                color = NestTextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título da Ideia") },
                placeholder = { Text("Ex: Reduzir tempo de embarque") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = NestWhite,
                    unfocusedContainerColor = NestWhite,
                    focusedBorderColor = NestGold,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Qual é o problema?") },
                placeholder = { Text("Descreva o problema que você identificou...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = NestWhite,
                    unfocusedContainerColor = NestWhite,
                    focusedBorderColor = NestGold,
                    unfocusedBorderColor = Color.Transparent
                ),
                maxLines = 6,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = guidelineMenuExpanded,
                onExpandedChange = { guidelineMenuExpanded = !guidelineMenuExpanded }
            ) {
                OutlinedTextField(
                    value = selectedGuidelineTitle,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Guideline estratégica") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = guidelineMenuExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NestWhite,
                        unfocusedContainerColor = NestWhite,
                        focusedBorderColor = NestGold,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    enabled = !isLoading
                )
                ExposedDropdownMenu(
                    expanded = guidelineMenuExpanded,
                    onDismissRequest = { guidelineMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Sem guideline") },
                        onClick = {
                            selectedGuidelineId = null
                            selectedGuidelineTitle = "Sem guideline"
                            guidelineMenuExpanded = false
                        }
                    )
                    guidelines.forEach { guideline ->
                        DropdownMenuItem(
                            text = { Text(guideline.title) },
                            onClick = {
                                selectedGuidelineId = guideline.id
                                selectedGuidelineTitle = guideline.title
                                guidelineMenuExpanded = false
                            }
                        )
                    }
                }
            }

            if (guidelinesError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(guidelinesError, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (actionState is IdeaActionState.Error) {
                Text(
                    text = actionState.message,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onSubmit(title, description, selectedGuidelineId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NestGold),
                enabled = title.isNotBlank() &&
                        description.isNotBlank() &&
                        !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = NestWhite,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Enviar para o Ninho",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NestWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateIdeaScreenPreview() {
    NestTheme {
        CreateIdeaScreenContent(
            actionState = IdeaActionState.Idle,
            guidelines = emptyList(),
            onNavigateBack = {},
            onSubmit = { _, _, _ -> }
        )
    }
}