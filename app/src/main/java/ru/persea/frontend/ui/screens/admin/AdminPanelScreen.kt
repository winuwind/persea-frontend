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
import ru.persea.frontend.data.model.products.BrandDto
import ru.persea.frontend.data.model.products.CategoryDto
import ru.persea.frontend.ui.screens.viewModel.AdminPanelViewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.text.font.FontWeight
import ru.persea.frontend.data.model.products.BooleanFactorInput
import ru.persea.frontend.data.model.products.EnumFactorInput
import ru.persea.frontend.data.model.products.Factor
import ru.persea.frontend.data.model.products.NumericFactorInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(viewModel: AdminPanelViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val factors by viewModel.factors.collectAsState()
    val units by viewModel.units.collectAsState()
    val types by viewModel.types.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val brands by viewModel.brands.collectAsState()
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllData()
    }

    val tabs = listOf("Факторы", "Единицы", "Типы", "Категории", "Бренды", "Продукты")

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
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                maxLines = 1
                            )
                        }
                    )
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
                    3 -> CategoriesManagementTab(viewModel, categories)
                    4 -> BrandsManagementTab(viewModel, brands)
                    5 -> ProductsManagementTab(viewModel, products, categories, brands)
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
fun CategoriesManagementTab(
    viewModel: AdminPanelViewModel,
    categories: List<CategoryDto>
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<CategoryDto?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Создать категорию")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCategory = category }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(category.name ?: "Без названия", style = MaterialTheme.typography.titleMedium)
                        Text("ID: ${category.id}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CategoryDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { name, code ->
                viewModel.createCategory(name, code)
                showCreateDialog = false
            }
        )
    }

    selectedCategory?.let { category ->
        CategoryEditDialog(
            category = category,
            onDismiss = { selectedCategory = null },
            onSave = { name, code ->
                category.id?.let { viewModel.updateCategory(it, name, code) }
                selectedCategory = null
            },
            onDelete = {
                category.id?.let { viewModel.deleteCategory(it) }
                selectedCategory = null
            }
        )
    }
}

@Composable
fun BrandsManagementTab(
    viewModel: AdminPanelViewModel,
    brands: List<BrandDto>
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedBrand by remember { mutableStateOf<BrandDto?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Создать бренд")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(brands) { brand ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedBrand = brand }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(brand.name ?: "Без названия", style = MaterialTheme.typography.titleMedium)
                        brand.description?.let {
                            Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        BrandDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { name, description ->
                viewModel.createBrand(name, description)
                showCreateDialog = false
            }
        )
    }

    selectedBrand?.let { brand ->
        BrandEditDialog(
            brand = brand,
            onDismiss = { selectedBrand = null },
            onSave = { name, description ->
                brand.id?.let { viewModel.updateBrand(it, name, description) }
                selectedBrand = null
            },
            onDelete = {
                brand.id?.let { viewModel.deleteBrand(it) }
                selectedBrand = null
            }
        )
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
fun ProductsManagementTab(
    viewModel: AdminPanelViewModel,
    products: List<ru.persea.frontend.data.model.products.ProductDto>,
    categories: List<CategoryDto>,
    brands: List<BrandDto>
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<ru.persea.frontend.data.model.products.ProductDto?>(null) }
    val allFactors by viewModel.allFactors.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Создать продукт")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedProduct = product }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(product.name ?: "Без названия", style = MaterialTheme.typography.titleMedium)
                        Text("Рейтинг: ${product.rating ?: 0}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        ProductDialog(
            onDismiss = { showCreateDialog = false },
            onSave = { name, categoryId, brandId, imageURI, barcode, numericFactors, booleanFactors, enumFactors ->
                viewModel.createProduct(name, categoryId, brandId, imageURI, barcode, numericFactors, booleanFactors, enumFactors)
                showCreateDialog = false
            },
            categories = categories,
            brands = brands,
            allFactors = allFactors
        )
    }

    selectedProduct?.let { product ->
        ProductInfoDialog(
            product = product,
            onDismiss = { selectedProduct = null },
            onDelete = {
                product.id?.let { viewModel.deleteProduct(it) }
                selectedProduct = null
            }
        )
    }
}

@Composable
fun CategoryDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создать категорию") },
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
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Код") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name, code) }) {
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
fun CategoryEditDialog(
    category: CategoryDto,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(category.name ?: "") }
    var code by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать категорию") },
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
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Код") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = false
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name, code) }) {
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
fun BrandDialog(
    onDismiss: () -> Unit,
    onSave: (String, String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создать бренд") },
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
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name, description.ifBlank { null }) }) {
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
fun BrandEditDialog(
    brand: BrandDto,
    onDismiss: () -> Unit,
    onSave: (String, String?) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(brand.name ?: "") }
    var description by remember { mutableStateOf(brand.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать бренд") },
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
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name, description.ifBlank { null }) }) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDialog(
    onDismiss: () -> Unit,
    onSave: (String, Long, Long, String?, String?, List<NumericFactorInput>?, List<BooleanFactorInput>?, List<EnumFactorInput>?) -> Unit,
    categories: List<CategoryDto>,
    brands: List<BrandDto>,
    allFactors: List<Factor>
) {
    var name by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var selectedBrandId by remember { mutableStateOf<Long?>(null) }
    var imageURI by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }

    // Факторы
    var numericFactors by remember { mutableStateOf<List<NumericFactorInput>>(emptyList()) }
    var booleanFactors by remember { mutableStateOf<List<BooleanFactorInput>>(emptyList()) }
    var enumFactors by remember { mutableStateOf<List<EnumFactorInput>>(emptyList()) }

    // UI состояния
    var categoryExpanded by remember { mutableStateOf(false) }
    var brandExpanded by remember { mutableStateOf(false) }
    var showNumericFactorsDialog by remember { mutableStateOf(false) }
    var showBooleanFactorsDialog by remember { mutableStateOf(false) }
    var showEnumFactorsDialog by remember { mutableStateOf(false) }

    // Разделяем факторы по типам
    val numericFactorList = allFactors.filter { it.type?.id == 1L }
    val booleanFactorList = allFactors.filter { it.type?.id == 2L }
    val enumFactorList = allFactors.filter { it.type?.id == 3L }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создать продукт") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Category dropdown
                Text("Категория", style = MaterialTheme.typography.labelMedium)
                Button(
                    onClick = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = categories.find { it.id == selectedCategoryId }?.name ?: "Выберите категорию",
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        if (categoryExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name ?: "") },
                            onClick = {
                                selectedCategoryId = category.id
                                categoryExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Brand dropdown
                Text("Бренд", style = MaterialTheme.typography.labelMedium)
                Button(
                    onClick = { brandExpanded = !brandExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = brands.find { it.id == selectedBrandId }?.name ?: "Выберите бренд",
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        if (brandExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = brandExpanded,
                    onDismissRequest = { brandExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    brands.forEach { brand ->
                        DropdownMenuItem(
                            text = { Text(brand.name ?: "") },
                            onClick = {
                                selectedBrandId = brand.id
                                brandExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = imageURI,
                    onValueChange = { imageURI = it },
                    label = { Text("URL изображения (опционально)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    label = { Text("Штрих-код (опционально)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Числовые факторы
                Text("Числовые факторы", style = MaterialTheme.typography.titleSmall)
                Button(
                    onClick = { showNumericFactorsDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${numericFactors.size} фактор(ов) выбрано")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Логические факторы
                Text("Логические факторы", style = MaterialTheme.typography.titleSmall)
                Button(
                    onClick = { showBooleanFactorsDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${booleanFactors.size} фактор(ов) выбрано")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Перечисляемые факторы
                Text("Перечисляемые факторы", style = MaterialTheme.typography.titleSmall)
                Button(
                    onClick = { showEnumFactorsDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${enumFactors.size} фактор(ов) выбрано")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && selectedCategoryId != null && selectedBrandId != null) {
                        onSave(
                            name,
                            selectedCategoryId!!,
                            selectedBrandId!!,
                            imageURI.ifBlank { null },
                            barcode.ifBlank { null },
                            numericFactors,
                            booleanFactors,
                            enumFactors
                        )
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

    // Диалог выбора числовых факторов
    if (showNumericFactorsDialog) {
        NumericFactorsDialog(
            factors = numericFactorList,
            selectedFactors = numericFactors,
            onDismiss = { showNumericFactorsDialog = false },
            onConfirm = { selected ->
                numericFactors = selected
                showNumericFactorsDialog = false
            }
        )
    }

    // Диалог выбора логических факторов
    if (showBooleanFactorsDialog) {
        BooleanFactorsDialog(
            factors = booleanFactorList,
            selectedFactors = booleanFactors,
            onDismiss = { showBooleanFactorsDialog = false },
            onConfirm = { selected ->
                booleanFactors = selected
                showBooleanFactorsDialog = false
            }
        )
    }

    // Диалог выбора перечисляемых факторов
    if (showEnumFactorsDialog) {
        EnumFactorsDialog(
            factors = enumFactorList,
            selectedFactors = enumFactors,
            onDismiss = { showEnumFactorsDialog = false },
            onConfirm = { selected ->
                enumFactors = selected
                showEnumFactorsDialog = false
            }
        )
    }
}


@Composable
fun NumericFactorsDialog(
    factors: List<Factor>,
    selectedFactors: List<NumericFactorInput>,
    onDismiss: () -> Unit,
    onConfirm: (List<NumericFactorInput>) -> Unit
) {
    var tempSelected by remember { mutableStateOf(selectedFactors.toMutableList()) }
    var currentFactorId by remember { mutableStateOf<Long?>(null) }
    var currentAmount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Числовые факторы") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                factors.forEach { factor ->
                    val existing = tempSelected.find { it.factorId == factor.id }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(factor.name ?: "Фактор", fontWeight = FontWeight.Bold)
                                if (existing != null) {
                                    Text(
                                        text = "${existing.amount}",
                                        color = Color.Green
                                    )
                                    IconButton(onClick = {
                                        tempSelected.removeAll { it.factorId == factor.id }
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                    }
                                } else {
                                    IconButton(onClick = {
                                        currentFactorId = factor.id
                                        currentAmount = ""
                                    }) {
                                        Icon(Icons.Default.Add, contentDescription = "Добавить")
                                    }
                                }
                            }

                            if (currentFactorId == factor.id) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = currentAmount,
                                        onValueChange = { currentAmount = it },
                                        label = { Text("Значение") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    IconButton(onClick = {
                                        val amount = currentAmount.toDoubleOrNull()
                                        if (amount != null) {
                                            tempSelected.add(NumericFactorInput(factor.id, amount))
                                            currentFactorId = null
                                            currentAmount = ""
                                        }
                                    }) {
                                        Icon(Icons.Default.Check, contentDescription = "OK")
                                    }
                                    IconButton(onClick = {
                                        currentFactorId = null
                                        currentAmount = ""
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = "Отмена")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempSelected) }) {
                Text("Готово")
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
fun BooleanFactorsDialog(
    factors: List<Factor>,
    selectedFactors: List<BooleanFactorInput>,
    onDismiss: () -> Unit,
    onConfirm: (List<BooleanFactorInput>) -> Unit
) {
    var tempSelected by remember { mutableStateOf(selectedFactors.toMutableList()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Логические факторы") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                factors.forEach { factor ->
                    val existing = tempSelected.find { it.factorId == factor.id }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(factor.name ?: "Фактор", modifier = Modifier.weight(1f))

                            if (existing != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(if (existing.value == true) "Да" else "Нет", color = Color.Green)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(onClick = {
                                        tempSelected.removeAll { it.factorId == factor.id }
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                    }
                                }
                            } else {
                                Row {
                                    IconButton(onClick = {
                                        tempSelected.add(BooleanFactorInput(factor.id, true))
                                    }) {
                                        Text("Да")
                                    }
                                    IconButton(onClick = {
                                        tempSelected.add(BooleanFactorInput(factor.id, false))
                                    }) {
                                        Text("Нет")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempSelected) }) {
                Text("Готово")
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
fun EnumFactorsDialog(
    factors: List<Factor>,
    selectedFactors: List<EnumFactorInput>,
    onDismiss: () -> Unit,
    onConfirm: (List<EnumFactorInput>) -> Unit
) {
    var tempSelected by remember { mutableStateOf(selectedFactors.toMutableList()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Перечисляемые факторы") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                factors.forEach { factor ->
                    val existing = tempSelected.find { it.factorId == factor.id }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(factor.name ?: "Фактор", fontWeight = FontWeight.Bold)

                            if (existing != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Выбрано", color = Color.Green)
                                    IconButton(onClick = {
                                        tempSelected.removeAll { it.factorId == factor.id }
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                    }
                                }
                            } else {
                                TextButton(
                                    onClick = {
                                        // Для enum пока просто добавляем заглушку
                                        // В реальном приложении нужно получать список enum значений
                                        tempSelected.add(EnumFactorInput(factor.id, 1))
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Выбрать значение")
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempSelected) }) {
                Text("Готово")
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
fun ProductInfoDialog(
    product: ru.persea.frontend.data.model.products.ProductDto,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Информация о продукте") },
        text = {
            Column {
                Text("Название: ${product.name ?: "Не указано"}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Рейтинг: ${product.rating ?: 0}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("ID: ${product.id}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text("Удалить", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
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