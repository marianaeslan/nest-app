package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.viewmodel.AuthViewModel
import br.com.gabgrupo.nest.viewmodel.UserCreationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    name: String,
    role: UserRole,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val creationState by viewModel.userCreationState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(creationState) {
        if (creationState is UserCreationState.Success) {
            showCreateDialog = false
            viewModel.resetUserCreationState()
        }
    }

    Scaffold(
        topBar = { NestTopAppBar(title = { Text("Perfil", color = NestNavy) }) },
        bottomBar = {
            NestBottomNavBar(
                currentRoute = NavItem.PROFILE,
                userRole = role,
                onNavigate = onNavigate,
                onFabClick = {}
            )
        },
        containerColor = NestBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                // Header com avatar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Avatar circular
                    Surface(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        color = getUserAvatarColor(name),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.firstOrNull()?.toString()?.uppercase() ?: "U",
                                color = NestWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 48.sp
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = NestNavy
                        )
                        Surface(
                            modifier = Modifier.clip(RoundedCornerShape(24.dp)),
                            color = NestGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                text = getRoleLabel(role),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = NestGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            item {
                // Card de informações
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NestWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProfileInfoItem(
                            label = "Nome",
                            value = name
                        )
                        Divider(color = Color(0xFFF1F1F1))
                        ProfileInfoItem(
                            label = "Função",
                            value = getRoleLabel(role)
                        )
                        Divider(color = Color(0xFFF1F1F1))
                        ProfileInfoItem(
                            label = "Tipo de acesso",
                            value = getRoleAccessLevel(role)
                        )
                    }
                }
            }

            item {
                // Botões de ação
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (role == UserRole.LEADER) {
                        Button(
                            onClick = { showCreateDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NestGold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Adicionar novo colaborador",
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = NestNavy,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.logout(onLogout) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Logout",
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = NestWhite,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showCreateDialog) {
        CreateCollaboratorDialog(
            state = creationState,
            onDismiss = { showCreateDialog = false },
            onCreate = { collaboratorName, email, collaboratorRole ->
                viewModel.createUser(collaboratorName, email, collaboratorRole)
            }
        )
    }
}

@Composable
private fun ProfileInfoItem(
    label: String,
    value: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = NestTextSecondary,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = NestNavy,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCollaboratorDialog(
    state: UserCreationState,
    onDismiss: () -> Unit,
    onCreate: (String, String, UserRole) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(UserRole.OPERATOR) }
    var expanded by remember { mutableStateOf(false) }
    val error = (state as? UserCreationState.Error)?.message

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo colaborador") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
                    OutlinedTextField(
                        value = role.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Role") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded, { expanded = false }) {
                        UserRole.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.name) },
                                onClick = { role = option; expanded = false }
                            )
                        }
                    }
                }
                if (error != null) {
                    Text(
                        error,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = state !is UserCreationState.Loading && name.isNotBlank() && email.isNotBlank(),
                onClick = { onCreate(name, email, role) },
                colors = ButtonDefaults.buttonColors(containerColor = NestGold)
            ) { Text("Criar") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancelar") } }
    )
}

private fun getUserAvatarColor(userName: String): Color {
    val colors = listOf(
        Color(0xFF7C3AED),  // Purple
        Color(0xFF3B82F6),  // Blue
        Color(0xFF10B981),  // Green
        Color(0xFFF59E0B),  // Amber
        Color(0xFFEF4444)   // Red
    )
    return colors[userName.hashCode() % colors.size]
}

private fun getRoleLabel(role: UserRole): String = when (role) {
    UserRole.MANAGER -> "Gerente"
    UserRole.OPERATOR -> "Operador"
    UserRole.LEADER -> "Lider"
}

private fun getRoleAccessLevel(role: UserRole): String = when (role) {
    UserRole.MANAGER -> "Acesso completo - Gerenciador"
    UserRole.OPERATOR -> "Acesso padrão - Operacional"
    UserRole.LEADER -> "Acesso corporativo - Métricas"
}