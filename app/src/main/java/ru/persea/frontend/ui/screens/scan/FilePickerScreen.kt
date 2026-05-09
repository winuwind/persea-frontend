package ru.persea.frontend.ui.screens.scan

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
fun FilePickerScreen(
    onBarcodeScanned: (String) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                // Получаем изображение из URI
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                // Сканируем штрих-код
                val image = InputImage.fromBitmap(bitmap, 0)
                val scanner = BarcodeScanning.getClient()

                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        barcodes.firstOrNull()?.rawValue?.let { barcode ->
                            onBarcodeScanned(barcode)
                        } ?: run {
                            // Если штрих-код не найден, можно показать ошибку
                            onBarcodeScanned("")
                        }
                    }
                    .addOnFailureListener {
                        onBarcodeScanned("")
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                onBarcodeScanned("")
            }
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch("image/*")
    }
}