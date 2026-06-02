package ru.persea.frontend.ui.screens.scan

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.nio.ByteBuffer
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraScreen(
    onClose: () -> Unit,
    onBarcodeScanned: (String) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val configuration = LocalConfiguration.current

    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    // Создаем Preview
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    // Создаем анализатор изображений для штрих-кодов
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    val barcodeScanner = BarcodeScanning.getClient()

                    val analysisExecutor = Executors.newSingleThreadExecutor()

                    imageAnalysis.setAnalyzer(analysisExecutor) { imageProxy ->
                        scanBarcode(imageProxy, barcodeScanner) { barcode ->
                            // Останавливаем анализ после нахождения штрих-кода
                            imageProxy.close()
                            onBarcodeScanned(barcode)
                        }
                        imageProxy.close()
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )

                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        FloatingActionButton(
            onClick = {
                // Мануальный захват фото и сканирование
                // (опционально, если нужен ручной режим)
            },
            modifier = Modifier
                .align(
                    if (isPortrait) Alignment.BottomCenter
                    else Alignment.CenterEnd
                )
                .padding(
                    bottom = if (isPortrait) 32.dp else 0.dp,
                    end = if (!isPortrait) 32.dp else 0.dp
                )
        ) {
            Text("SCAN")
        }

        Text(
            "Close",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable { onClose() }
        )
    }
}

// Функция сканирования штрих-кода из ImageProxy
@OptIn(ExperimentalGetImage::class)
private fun scanBarcode(
    imageProxy: ImageProxy,
    scanner: BarcodeScanner,
    onResult: (String) -> Unit
) {
    val mediaImage = imageProxy.image ?: return

    val inputImage = InputImage.fromMediaImage(
        mediaImage,
        imageProxy.imageInfo.rotationDegrees
    )

    scanner.process(inputImage)
        .addOnSuccessListener { barcodes ->
            barcodes.firstOrNull()?.rawValue?.let { barcode ->
                onResult(barcode)
            }
        }
        .addOnFailureListener { exception ->
            // Логирование ошибки
            exception.printStackTrace()
        }
}