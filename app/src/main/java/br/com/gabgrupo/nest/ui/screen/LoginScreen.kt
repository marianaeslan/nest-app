package br.com.gabgrupo.nest.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gabgrupo.nest.R
import br.com.gabgrupo.nest.ui.theme.NestGold
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTheme
import br.com.gabgrupo.nest.ui.theme.NestWhite
import br.com.gabgrupo.nest.viewmodel.AuthState
import br.com.gabgrupo.nest.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: (String, String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    var showLoginForm by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            val successState = state as AuthState.Success
            onLoginSuccess(
                successState.role,
                successState.name
            )
        }
    }

    LoginScreenContent(
        authState = state,
        showLoginForm = showLoginForm,
        onEnterClick = {
            showLoginForm = true
        },
        onBackClick = {
            showLoginForm = false
        },
        onLoginClick = { email, password ->
            viewModel.login(email, password)
        }
    )
}

@Composable
private fun LoginScreenContent(
    authState: AuthState,
    showLoginForm: Boolean,
    onEnterClick: () -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: (String, String) -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NestNavy)
    ) {

        // Imagem da águia preenchendo a tela inteira
        Image(
            painter = painterResource(R.drawable.nest_eagle),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Camada de sobreposição para garantir legibilidade
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    NestNavy.copy(alpha = 0.4f)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 38.dp,
                    vertical = 60.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            Image(
                painter = painterResource(R.drawable.nest_logo),
                contentDescription = "Nest",
                modifier = Modifier
                    .size(
                        width = 190.dp,
                        height = 110.dp
                    ),
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            if (showLoginForm) {

                LoginForm(
                    email = email,
                    password = password,
                    authState = authState,
                    onEmailChange = {
                        email = it
                    },
                    onPasswordChange = {
                        password = it
                    },
                    onLoginClick = {
                        onLoginClick(email, password)
                    },
                    onBackClick = onBackClick
                )

            } else {

                WelcomeContent(
                    onEnterClick = onEnterClick
                )
            }
        }
    }
}

@Composable
private fun WelcomeContent(
    onEnterClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "Onde ideias\nganham voo.",
            color = NestWhite,
            fontSize = 32.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Colabore. Inove.\nTransforme resultados.",
            color = NestWhite.copy(alpha = 0.85f),
            fontSize = 17.sp,
            lineHeight = 25.sp
        )

        Spacer(
            modifier = Modifier.height(58.dp)
        )

        Button(
            onClick = onEnterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NestGold,
                contentColor = NestWhite
            )
        ) {
            Text(
                text = "Entrar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "Saiba mais",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    // futuramente
                },
            color = NestWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LoginForm(
    email: String,
    password: String,
    authState: AuthState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = NestWhite,
        unfocusedTextColor = NestWhite,
        focusedBorderColor = NestWhite,
        unfocusedBorderColor = NestWhite.copy(alpha = 0.6f),
        focusedLabelColor = NestWhite,
        unfocusedLabelColor = NestWhite.copy(alpha = 0.6f),
        cursorColor = NestWhite,
        selectionColors = TextSelectionColors(
            handleColor = NestWhite,
            backgroundColor = NestWhite.copy(alpha = 0.4f)
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Bem-vindo ao Nest",
            color = NestWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Entre com suas credenciais",
            color = NestWhite.copy(alpha = 0.8f),
            fontSize = 15.sp
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = {
                Text("E-mail corporativo")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = {
                Text("Senha")
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        if (authState is AuthState.Error) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = authState.message,
                color = Color(0xFFFF6B6B),
                fontSize = 14.sp
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NestGold,
                contentColor = NestWhite
            ),
            enabled = authState !is AuthState.Loading
        ) {

            if (authState is AuthState.Loading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = NestWhite
                )

            } else {

                Text(
                    text = "Entrar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "Voltar",
            modifier = Modifier.clickable {
                onBackClick()
            },
            color = NestWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    NestTheme {
        LoginScreenContent(
            authState = AuthState.Idle,
            showLoginForm = false,
            onEnterClick = {},
            onBackClick = {},
            onLoginClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenFormPreview() {
    NestTheme {
        LoginScreenContent(
            authState = AuthState.Idle,
            showLoginForm = true,
            onEnterClick = {},
            onBackClick = {},
            onLoginClick = { _, _ -> }
        )
    }
}
