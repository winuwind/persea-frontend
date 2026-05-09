package ru.persea.frontend.ui.screens.scan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.persea.frontend.ui.screens.viewModel.BarcodeViewModel

@Composable
fun ScanScreen(
    viewModel: BarcodeViewModel = viewModel(),
    onProductClick: (Long) -> Unit
) {

    var showSheet by remember { mutableStateOf(true) }
    var mode by remember { mutableStateOf<String?>(null) }
    var scannedBarcode by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        when (mode) {
            "camera" -> CameraScreen(
                onClose = {
                    mode = null
                    showSheet = true
                },
                onBarcodeScanned = { barcode ->
                    scannedBarcode = barcode
                    mode = null
                    showSheet = true


                    viewModel.searchByBarcode(barcode) { foundProduct ->
                        foundProduct?.id?.let { productId ->
                            onProductClick(productId)
                        }
                    }
                }
            )

            "file" -> FilePickerScreen(
                onBarcodeScanned = { barcode ->
                    scannedBarcode = barcode
                    mode = null
                    showSheet = true

                    viewModel.searchByBarcode(barcode) { foundProduct ->
                        foundProduct?.id?.let { productId ->
                            onProductClick(productId)
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

        if (!showSheet && mode == null && scannedBarcode != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Scanned barcode:",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = scannedBarcode ?: "",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = {
                            val barcodeNumber = scannedBarcode?.toLongOrNull()
                            barcodeNumber?.let { onProductClick(it) }
                        }
                    ) {
                        Text("Search Product")
                    }
                }
            }
        } else if (!showSheet && mode == null) {
            Text(
                "Select scan method",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}