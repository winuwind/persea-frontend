package ru.persea.frontend.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.persea.frontend.ui.screens.viewModel.SearchViewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {

    var query by remember { mutableStateOf("") }
    var isSearchPerformed by remember { mutableStateOf(false) }
    val suggestions = viewModel.suggestions
    val results = viewModel.results

    Column(modifier = Modifier.padding(16.dp)) {

        Row {
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.onQueryChanged(it)
                    if (isSearchPerformed) {
                        isSearchPerformed = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search food...") }
            )

            Text(
                text = "Send",
                modifier = Modifier
                    .clickable {
                        viewModel.onQuerySend(query)
                        isSearchPerformed = true
                    }
                    .padding(12.dp)
            )
        }

        LazyColumn {
            if (!isSearchPerformed) {
                items(suggestions) { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                query = item
                                viewModel.onQueryChanged(item)
                                viewModel.onQuerySend(item)
                                isSearchPerformed = true
                            }
                            .padding(12.dp)
                    )
                }
            } else {
                items(results) { item ->
                    Text(
                        text = "${item.name}; Rating: ${item.rating}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Отобразить новую панель детальной информации о продукте
                            }
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}