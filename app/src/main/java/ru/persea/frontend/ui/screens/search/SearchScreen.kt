package ru.persea.frontend.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.persea.frontend.ui.screens.viewModel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onProductClick: (Long) -> Unit
) {

    var query by remember { mutableStateOf("") }
    var isSearchPerformed by remember { mutableStateOf(false) }
    val suggestions = viewModel.suggestions
    val results = viewModel.results

    Column(modifier = Modifier.padding(16.dp)) {

        Row (modifier = Modifier.height(60.dp)) {
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.onQueryChanged(it)
                    if (isSearchPerformed) {
                        isSearchPerformed = false
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                placeholder = { Text("Search food...") }
            )

            Button(
                onClick = {
                    viewModel.onQuerySend(query)
                    isSearchPerformed = true
                },
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 8.dp,
                    bottomEnd = 8.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00AFFF)
                )
            ) {
                Text(text = "Send")
            }
        }

        if (!isSearchPerformed && suggestions.isNotEmpty()) {
            LazyColumn {
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
            }
        }

        if (isSearchPerformed) {
            LazyColumn {
                items(results) { item ->
                    Text(
                        text = "${item.name}; Rating: ${item.rating}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                item.id?.let { onProductClick(it) }
                            }
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}