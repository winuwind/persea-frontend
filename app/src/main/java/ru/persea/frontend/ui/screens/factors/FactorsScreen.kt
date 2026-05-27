package ru.persea.frontend.ui.screens.factors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.persea.frontend.ui.screens.viewModel.FactorsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactorsScreen(viewModel: FactorsViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadFactors()
        viewModel.loadUnits()
        viewModel.loadTypes()
    }

    val factors = viewModel.factors
    val units = viewModel.units
    val types = viewModel.types
    val isLoading = viewModel.isLoading
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Факторы качества") })
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
            }

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                selectedTab == 0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(factors) { factor ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = factor.name ?: "Без названия", style = MaterialTheme.typography.titleSmall)
                                    factor.type?.name?.let { typeName ->
                                        Text(text = "Тип: $typeName", style = MaterialTheme.typography.bodySmall)
                                    }
                                    factor.description?.let { desc ->
                                        Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
                selectedTab == 1 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(units) { unit ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = unit.name ?: "Без названия",
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
                selectedTab == 2 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(types) { type ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = type.name ?: "Без названия",
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}