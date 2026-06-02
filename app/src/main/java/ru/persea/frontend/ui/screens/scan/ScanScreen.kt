package ru.persea.frontend.ui.screens.scan

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.persea.frontend.ui.screens.viewModel.BarcodeViewModel

@Composable
fun ScanScreen(
    viewModel: BarcodeViewModel = viewModel(),
    onProductClick: (Long) -> Unit
) {
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(true) }
    var mode by remember { mutableStateOf<String?>(null) }
    var isSearching by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        when (mode) {
            "camera" -> CameraScreen(
                onClose = {
                    mode = null
                    showSheet = true
                },
                onBarcodeScanned = { barcode ->
                    mode = null
                    showSheet = true
                    isSearching = true

                    viewModel.searchByBarcode(barcode) { foundProduct ->
                        isSearching = false
                        if (foundProduct != null && foundProduct.id != null) {
                            onProductClick(foundProduct.id)
                        } else {
                            Toast.makeText(context, "Продукт с таким штрих-кодом не найден", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )

            "file" -> FilePickerScreen(
                onBarcodeScanned = { barcode ->
                    mode = null
                    showSheet = true
                    isSearching = true

                    viewModel.searchByBarcode(barcode) { foundProduct ->
                        isSearching = false
                        if (foundProduct != null && foundProduct.id != null) {
                            onProductClick(foundProduct.id)
                        } else {
                            Toast.makeText(context, "Продукт с таким штрих-кодом не найден", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
        }

        if (showSheet) {
            ScanBottomSheet(
                onDismiss = {
                    showSheet = false
                },
                onSelect = {
                    mode = it
                    showSheet = false
                }
            )
        }

        if (isSearching) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Text(
                        text = "Поиск продукта...",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }

        if (!showSheet && mode == null && !isSearching) {
            Text(
                "Выберите способ сканирования",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}