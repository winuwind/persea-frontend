package ru.persea.frontend.ui.screens.support

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen() {
    val context = LocalContext.current
    var messageText by remember { mutableStateOf("") }
    var emailSubject by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    fun sendEmail() {
        if (messageText.isBlank()) {
            statusMessage = "Введите сообщение"
            return
        }

        isSending = true
        statusMessage = null

        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@persea.ru")
                putExtra(Intent.EXTRA_SUBJECT, if (emailSubject.isBlank()) "Обращение в поддержку Persea" else emailSubject)
                putExtra(Intent.EXTRA_TEXT, messageText)
            }
            context.startActivity(Intent.createChooser(intent, "Отправить сообщение"))
            statusMessage = "Выберите приложение для отправки"
            messageText = ""
            emailSubject = ""
        } catch (e: Exception) {
            statusMessage = "Ошибка: приложение для email не найдено"
        } finally {
            isSending = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Поддержка") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Свяжитесь с нами",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Напишите нам о любой проблеме или предложении",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = emailSubject,
                        onValueChange = { emailSubject = it },
                        label = { Text("Тема (опционально)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        label = { Text("Сообщение") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        minLines = 5
                    )

                    if (statusMessage != null) {
                        Text(
                            text = statusMessage ?: "",
                            fontSize = 12.sp,
                            color = if (statusMessage?.contains("Ошибка") == true)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = { sendEmail() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSending
                    ) {
                        Text(if (isSending) "Открывается почтовое приложение..." else "Отправить")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Или напишите напрямую:",
                        fontSize = 14.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Text(
                        text = "support@persea.ru",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}