package br.com.gabgrupo.nest.ui.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gabgrupo.nest.ui.theme.NestBackground
import br.com.gabgrupo.nest.ui.theme.NestNavy
import br.com.gabgrupo.nest.ui.theme.NestTextPrimary
import br.com.gabgrupo.nest.ui.theme.NestTextSecondary
import br.com.gabgrupo.nest.ui.theme.NestTheme

@Composable
fun NestSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit = {}
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "Buscar ideias", color = NestTextSecondary)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = NestTextSecondary
            )
        },
        trailingIcon = {
            IconButton(onClick = onFilterClick) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filtrar",
                    tint = NestNavy
                )
            }
        },
        shape = RoundedCornerShape(50), // Cantos bem arredondados
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NestBackground, // Fundo cinza muito claro
            unfocusedContainerColor = NestBackground,
            focusedBorderColor = Color.Transparent, // Ocultar borda para parecer "pílula"
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = NestTextPrimary,
            unfocusedTextColor = NestTextPrimary,
            cursorColor = NestNavy
        ),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun NestSearchBarPreview() {
    var query by remember { mutableStateOf("") }
    
    NestTheme {
        NestSearchBar(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier.padding(16.dp),
            onFilterClick = {}
        )
    }
}

