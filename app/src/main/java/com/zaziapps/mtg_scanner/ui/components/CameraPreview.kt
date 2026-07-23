package com.zaziapps.mtg_scanner.ui.components

import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.concurrent.futures.await
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Camera Preview component for photos
 * @param imageCapture ImageCapture | Image captured by user
 * @param modifier Modifier | View modifier
 */
@Composable
fun CameraPreview(
    imageCapture: ImageCapture,
    modifier: Modifier = Modifier
) {
    // Obtain app context
    val context = LocalContext.current
    // Obtain the life cycle of the screen to control camera on/off
    val lifecycleOwner = LocalLifecycleOwner.current
    // Obtain camera connection
    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }

    // Camera async configuration
    LaunchedEffect(lifecycleOwner) {
        try {
            // Initialization of Camera Preview
            val cameraProvider = ProcessCameraProvider.getInstance(context).await()
            val preview = Preview.Builder().build()

            preview.setSurfaceProvider { request ->
                surfaceRequest = request
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Log.e("CameraXError", "CameraX error", e)
        }
    }

    // Camera Preview Component
    surfaceRequest?.let { request ->
        CameraXViewfinder(
            surfaceRequest = request,
            modifier = modifier.fillMaxSize()
        )
    }
}
