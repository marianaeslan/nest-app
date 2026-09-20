package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.data.model.UserRole
import br.com.gabgrupo.nest.ui.shared.NavItem
import br.com.gabgrupo.nest.ui.shared.NestBottomNavBar
import br.com.gabgrupo.nest.ui.shared.NestTopAppBar
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
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
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(name, style = androidx.compose.material3.MaterialTheme.typography.headlineSmall, color = NestNavy)
            Text(role.name, color = NestGold)
            if (role == UserRole.LEADER) {
                Button(
                    onClick = { showCreateDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = NestGold),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Adicionar novo colaborador") }
            }
            Button(
                onClick = { viewModel.logout(onLogout) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E)),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Logout") }
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
                OutlinedTextField(name, { name = it }, label = { Text("Nome") })
                OutlinedTextField(email, { email = it }, label = { Text("E-mail") })
                ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
                    OutlinedTextField(
                        value = role.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Role") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
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
                if (error != null) Text(error, color = Color.Red)
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
