package ru.persea.frontend.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.persea.frontend.ui.screens.viewModel.AuthViewModel

@Composable
fun LogoutButton(
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            viewModel.logout()
            onLogout()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEF4444)
        )
    ) {
        Text("Выйти")
    }
}