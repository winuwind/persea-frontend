package ru.persea.frontend.ui.screens.scan

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScanScreen() {

    var showSheet by remember { mutableStateOf(true) }
    var mode by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        when (mode) {
            "camera" -> CameraScreen(
                onClose = {
                    mode = null
                    showSheet = true
                }
            )

            "file" -> FilePickerScreen(
                onDone = {
                    mode = null
                    showSheet = true
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

        if (!showSheet && mode == null) {
            Text(
                "Select scan method",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}