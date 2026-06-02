package ru.persea.frontend.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.persea.frontend.ui.screens.viewModel.AdminPanelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(viewModel: AdminPanelViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val factors by viewModel.factors.collectAsState()
    val units by viewModel.units.collectAsState()
    val types by viewModel.types.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Панель администратора") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text("Факторы", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text("Единицы", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                    Text("Типы", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }) {
                    Text("Продукты", modifier = Modifier.padding(16.dp))
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when (selectedTab) {
                    0 -> FactorsManagementTab(viewModel, factors)
                    1 -> UnitsManagementTab(viewModel, units)
                    2 -> TypesManagementTab(viewModel, types)
                    3 -> ProductsManagementTab(viewModel)
                }
            }

            message?.let {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearMessage() }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(it)
                }
            }
        }
    }
}

@Composable
fun FactorsManagementTab(
    viewModel: AdminPanelViewModel,
    factors: List<ru.persea.frontend.data.model.products.Factor>
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedFactor by remember { mutableStateOf<ru.persea.frontend.data.model.products.Factor?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Создать фактор")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(factors) { factor ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedFactor = factor }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(factor.name ?: "Без названия", style = MaterialTheme.typography.titleMedium)
                        Text("Тип: ${factor.type?.name ?: "Не указан"}", style = MaterialTheme.typography.bodySmall)
                        factor.description?.let {
                            Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        FactorDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { name, typeId, description ->
                viewModel.createFactor(name, typeId, description)
                showCreateDialog = false
            }
        )
    }

    selectedFactor?.let { factor ->
        FactorEditDialog(
            factor = factor,
            onDismiss = { selectedFactor = null },
            onSave = { name, typeId, description ->
                factor.id?.let { viewModel.updateFactor(it, name, typeId, description) }
                selectedFactor = null
            },
            onDelete = {
                factor.id?.let { viewModel.deleteFactor(it) }
                selectedFactor = null
            }
        )
    }
}

@Composable
fun UnitsManagementTab(
    viewModel: AdminPanelViewModel,
    units: List<ru.persea.frontend.data.model.products.Unit>
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf<ru.persea.frontend.data.model.products.Unit?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Создать единицу измерения")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(units) { unit ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedUnit = unit }
                ) {
                    Text(
                        text = unit.name ?: "Без названия",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        UnitDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { name ->
                viewModel.createUnit(name)
                showCreateDialog = false
            }
        )
    }

    selectedUnit?.let { unit ->
        UnitEditDialog(
            unit = unit,
            onDismiss = { selectedUnit = null },
            onSave = { name ->
                unit.id?.toInt()?.let { viewModel.updateUnit(it, name) }
                selectedUnit = null
            },
            onDelete = {
                unit.id?.toInt()?.let { viewModel.deleteUnit(it) }
                selectedUnit = null
            }
        )
    }
}

@Composable
fun TypesManagementTab(
    viewModel: AdminPanelViewModel,
    types: List<ru.persea.frontend.data.model.products.FactorType>
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<ru.persea.frontend.data.model.products.FactorType?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Создать тип фактора")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(types) { type ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedType = type }
                ) {
                    Text(
                        text = type.name ?: "Без названия",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        TypeDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { name ->
                viewModel.createFactorType(name)
                showCreateDialog = false
            }
        )
    }

    selectedType?.let { type ->
        TypeEditDialog(
            factorType = type,
            onDismiss = { selectedType = null },
            onSave = { name ->
                type.id?.toInt()?.let { viewModel.updateFactorType(it, name) }
                selectedType = null
            },
            onDelete = {
                type.id?.toInt()?.let { viewModel.deleteFactorType(it) }
                selectedType = null
            }
        )
    }
}

@Composable
fun ProductsManagementTab(viewModel: AdminPanelViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Управление категориями
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Категории", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                // Здесь можно добавить список категорий с возможностью редактирования
                Text("Функционал в разработке", color = Color.Gray)
            }
        }

        // Управление брендами
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Бренды", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Функционал в разработке", color = Color.Gray)
            }
        }

        // Управление продуктами
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Продукты", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Функционал в разработке", color = Color.Gray)
            }
        }
    }
}

@Composable
fun FactorDialog(
    onDismiss: () -> Unit,
    onSave: (String, Long, String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var typeId by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создать фактор") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = typeId,
                    onValueChange = { typeId = it },
                    label = { Text("ID типа (1 - numeric, 2 - boolean, 3 - enum)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание (опционально)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val id = typeId.toLongOrNull()
                    if (name.isNotBlank() && id != null) {
                        onSave(name, id, description.ifBlank { null })
                    }
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun FactorEditDialog(
    factor: ru.persea.frontend.data.model.products.Factor,
    onDismiss: () -> Unit,
    onSave: (String, Long, String?) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(factor.name ?: "") }
    var typeId by remember { mutableStateOf(factor.type?.id?.toString() ?: "") }
    var description by remember { mutableStateOf(factor.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать фактор") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = typeId,
                    onValueChange = { typeId = it },
                    label = { Text("ID типа (1 - numeric, 2 - boolean, 3 - enum)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание (опционально)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val id = typeId.toLongOrNull()
                    if (name.isNotBlank() && id != null) {
                        onSave(name, id, description.ifBlank { null })
                    }
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDelete) {
                    Text("Удалить", color = Color.Red)
                }
                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        }
    )
}

@Composable
fun UnitDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создать единицу измерения") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name) }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun UnitEditDialog(
    unit: ru.persea.frontend.data.model.products.Unit,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(unit.name ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать единицу измерения") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name) }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDelete) {
                    Text("Удалить", color = Color.Red)
                }
                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        }
    )
}

@Composable
fun TypeDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создать тип фактора") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name) }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun TypeEditDialog(
    factorType: ru.persea.frontend.data.model.products.FactorType,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(factorType.name ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать тип фактора") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name) }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDelete) {
                    Text("Удалить", color = Color.Red)
                }
                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        }
    )
}