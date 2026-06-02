package ru.persea.frontend.ui.screens.studio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.persea.frontend.data.api.auth.TokenManager
import ru.persea.frontend.ui.screens.viewModel.StudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(viewModel: StudioViewModel = viewModel(), navController: NavController) {
    val brandName by viewModel.brandName.collectAsState()
    val brandDescription by viewModel.brandDescription.collectAsState()
    val categoryName by viewModel.categoryName.collectAsState()
    val categoryCode by viewModel.categoryCode.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasModeratorRole by viewModel.hasModeratorRole.collectAsState()
    val hasAdminRole by viewModel.hasAdminRole.collectAsState()
    val checkingRoles by viewModel.checkingRoles.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setCheckingRoles(true)
        try {
            val token = TokenManager.accessToken
            if (!token.isNullOrBlank()) {
                val parts = token.split(".")
                if (parts.size == 3) {
                    val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
                    val mapper = jacksonObjectMapper()
                    val json: Map<String, Any> = mapper.readValue(payload)
                    val realmAccess = json["realm_access"] as? Map<*, *>
                    val roles: List<*> = realmAccess?.get("roles") as? List<*> ?: listOf<String>()
                    val hasModerator = roles.contains("moderator") || roles.contains("admin")
                    val hasAdmin = roles.contains("admin")
                    viewModel.setRoles(hasModerator, hasAdmin)
                } else {
                    viewModel.setRoles(false, false)
                }
            } else {
                viewModel.setRoles(false, false)
            }
        } catch (e: Exception) {
            viewModel.setRoles(false, false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Студия данных") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (checkingRoles) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (!hasModeratorRole) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Доступ запрещён",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Требуется роль moderator или admin",
                        color = Color.Gray
                    )
                }
            } else {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (message != null && message!!.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = androidx.compose.material3.CardDefaults.cardColors(
                                    containerColor = if (message!!.contains("Ошибка"))
                                        MaterialTheme.colorScheme.errorContainer
                                    else
                                        MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = message!!,
                                    modifier = Modifier.padding(12.dp),
                                    color = if (message!!.contains("Ошибка"))
                                        MaterialTheme.colorScheme.onErrorContainer
                                    else
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("Создать бренд", style = MaterialTheme.typography.titleMedium)
                                OutlinedTextField(
                                    value = brandName,
                                    onValueChange = { viewModel.updateBrandName(it) },
                                    label = { Text("Название бренда") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = brandDescription,
                                    onValueChange = { viewModel.updateBrandDescription(it) },
                                    label = { Text("Описание (опционально)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Button(
                                    onClick = { viewModel.createBrand() },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !isLoading
                                ) {
                                    Text("Создать бренд")
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("Создать категорию", style = MaterialTheme.typography.titleMedium)
                                OutlinedTextField(
                                    value = categoryName,
                                    onValueChange = { viewModel.updateCategoryName(it) },
                                    label = { Text("Название категории") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = categoryCode,
                                    onValueChange = { viewModel.updateCategoryCode(it) },
                                    label = { Text("Код категории (например, WATER)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Button(
                                    onClick = { viewModel.createCategory() },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !isLoading
                                ) {
                                    Text("Создать категорию")
                                }
                            }
                        }
                    }

                    if (hasAdminRole) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text("Рекомендации", style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        text = "Пересчет ленты рекомендаций на основе действий пользователей",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    Button(
                                        onClick = { viewModel.recalculateRecommendations() },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = !isLoading
                                    ) {
                                        Text("Пересчитать рекомендации")
                                    }
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = { navController.navigate("admin") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF9C27B0)
                                )
                            ) {
                                Text("🔧 Панель администратора")
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}