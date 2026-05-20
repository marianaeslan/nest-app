package br.com.gabgrupo.nest.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextPrimary
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestWhite
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import br.com.gabgrupo.nest.ui.theme.NestTheme

@Composable
fun NestTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    maxLines: Int = 1,
    maxLength: Int? = null,
    singleLine: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (maxLength == null || newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            },
            label = {
                Text(
                    text = label,
                    color = NestTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            },
            placeholder = {
                Text(
                    text = placeholder,
                    color = NestTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            maxLines = maxLines,
            singleLine = singleLine,
            trailingIcon = if (isPassword) {
                {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Ocultar senha" else "Mostrar senha"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description, tint = NestTextSecondary)
                    }
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NestWhite,
                unfocusedContainerColor = NestBackground,
                focusedTextColor = NestTextPrimary,
                unfocusedTextColor = NestTextPrimary,
                focusedBorderColor = NestNavy,
                unfocusedBorderColor = NestTextSecondary
            ),
            textStyle = TextStyle(
                color = NestTextPrimary,
                fontWeight = FontWeight.Normal
            )
        )
        if (maxLength != null) {
            Text(
                text = "${value.length}/$maxLength",
                color = NestTextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = 4.dp),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NestTextFieldPreview() {
    NestTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            NestTextField(
                value = "",
                onValueChange = {},
                label = "E-mail",
                placeholder = "digite seu e-mail"
            )
            Spacer(modifier = Modifier.height(16.dp))
            NestTextField(
                value = "senha123",
                onValueChange = {},
                label = "Senha",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            NestTextField(
                value = "Explicação da ideia",
                onValueChange = {},
                label = "Descrição",
                maxLines = 5,
                maxLength = 300,
                singleLine = false
            )
        }
    }
}
