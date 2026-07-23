package com.zaziapps.mtg_scanner.ui.screens

import com.zaziapps.mtg_scanner.R
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.zaziapps.mtg_scanner.ocr.OcrViewModel
import com.zaziapps.mtg_scanner.scryfall.ScryfallViewModel
import com.zaziapps.mtg_scanner.data.model.LanguageItem
import com.zaziapps.mtg_scanner.data.model.MagicCard
import com.zaziapps.mtg_scanner.ui.components.CameraPreview
import com.zaziapps.mtg_scanner.ui.components.LanguageSelector
import com.zaziapps.mtg_scanner.ui.components.ScanConfirmationDialog
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.zaziapps.mtg_scanner.ui.components.LoadingDialog
import com.zaziapps.mtg_scanner.ui.themes.Black
import com.zaziapps.mtg_scanner.ui.themes.CardDetailBackground
import com.zaziapps.mtg_scanner.ui.themes.Brown
import com.zaziapps.mtg_scanner.ui.themes.DarkBrown
import com.zaziapps.mtg_scanner.ui.themes.DarkRed
import com.zaziapps.mtg_scanner.ui.themes.Gold
import com.zaziapps.mtg_scanner.ui.themes.LightBrown
import com.zaziapps.mtg_scanner.ui.themes.Red

/**
 * Camera preview screen that handles card detection, OCR, and language translation.
 * @param ocrViewModel ViewModel handling optical character recognition
 * @param scryfallViewModel ViewModel interacting with the Scryfall API
 * @param onManaDetected Callback triggered when a card and its matching layout profile are identified
 */
@Composable
fun CameraView(
    ocrViewModel: OcrViewModel = viewModel(),
    scryfallViewModel: ScryfallViewModel = viewModel(),
    onManaDetected: (CardDetailBackground, MagicCard) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageCapture = remember { ImageCapture.Builder().build() }

    val spanishLabel = stringResource(id = R.string.spanish_label)
    val englishLabel = stringResource(id = R.string.english_label)
    val germanLabel = stringResource(id = R.string.german_label)
    val italianLabel = stringResource(id = R.string.italian_label)
    val frenchLabel = stringResource(id = R.string.french_label)

    val languagesList = remember {
        listOf(
            LanguageItem("es", spanishLabel),
            LanguageItem("en", englishLabel),
            LanguageItem("de", germanLabel),
            LanguageItem("it", italianLabel),
            LanguageItem("fr", frenchLabel)
        )
    }

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoadingSpell by remember { mutableStateOf(false) }
    var languageInput by remember { mutableStateOf(languagesList.first()) }
    var languageOutput by remember { mutableStateOf(languagesList.first()) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasCameraPermission = isGranted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // --- MAIN WRAPPER ---
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Tainted dark mask
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
        )

        // --- CONTENT ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = stringResource(id = R.string.camera_view_title).uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color = Gold,
                modifier = Modifier.padding(top = 32.dp)
            )

            // 1. Camera Preview (5:7)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(5f / 7f) //
                    .clip(RoundedCornerShape(18.dp))
                    .border(
                        width = 3.dp,
                        color = Gold.copy(alpha = 0.8f), // Golden border
                        shape = RoundedCornerShape(18.dp)
                    )
                    .background(Black),
                contentAlignment = Alignment.Center
            ) {
                if (hasCameraPermission) {
                    CameraPreview(imageCapture = imageCapture, modifier = Modifier.fillMaxSize())

                    // Framing
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.required_access_camera_message),
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- LANGUAGE SELECTORS ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = DarkBrown.copy(alpha = 0.85f), // Brown/Burtn border style
                border = BorderStroke(1.dp, Brown) // Separator
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LanguageSelector(
                        selectedLanguage = languageInput,
                        languages = languagesList,
                        onLanguageSelected = { selectedLanguage -> languageInput = languagesList.first { it.displayName == selectedLanguage } },
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .wrapContentWidth()
                    )

                    // Stylized reversal button
                    IconButton(
                        onClick = {
                            val temp = languageInput
                            languageInput = languageOutput
                            languageOutput = temp
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Black, shape = CircleShape)
                            .border(1.dp, LightBrown, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                            contentDescription = stringResource(id = R.string.invert_lang_text),
                            tint = Gold // Golden icon
                        )
                    }

                    LanguageSelector(
                        selectedLanguage = languageOutput,
                        languages = languagesList,
                        onLanguageSelected = { selectedLanguage -> languageOutput = languagesList.first { it.displayName == selectedLanguage } },
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .wrapContentWidth()
                    )
                }
            }

            // Capture button
            Button(
                onClick = {
                    takePhoto(context, imageCapture) { bitmap ->
                        capturedBitmap = bitmap
                        showDialog = true
                    }
                },
                enabled = hasCameraPermission,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(58.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(29.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkRed, // Dark Red
                    disabledContainerColor = Color.Gray.copy(alpha = 0.5f),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 2.dp
                ),
                border = BorderStroke(1.5.dp, Red.copy(alpha = 0.6f)) // Subtle shimmer
            ) {
                Text(
                    text = stringResource(id = R.string.photo_button_text),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                )
            }
        }
    }

    if (showDialog && capturedBitmap != null) {
        // Photo confirmation dialog
        ScanConfirmationDialog(
            showDialog = showDialog,
            capturedBitmap = capturedBitmap,
            onDismiss = { showDialog = false },
            onScanClick = { bitmap ->
                coroutineScope.launch {
                    // Manage dialogs states
                    showDialog = false
                    isLoadingSpell = true

                    // Scan card process
                    try {
                        val cardName = ocrViewModel.firstRelevantTextExtraction(bitmap)
                        val cardResult = scryfallViewModel.searchCardByName(cardName, languageInput.code, languageOutput.code)

                        // If a card was found, etract color and send info to CardDetail view
                        if (cardResult != null) {
                            val background = scryfallViewModel.extractMainColor(cardResult)
                            onManaDetected(background, cardResult) // Send data to CardDetail view
                        }
                    } catch (e: Exception) {
                        Log.e("CameraView", "OCR/Scryfall Error", e)
                    } finally {
                        isLoadingSpell = false
                    }
                }
            }
        )
    }
    LoadingDialog(isLoading = isLoadingSpell)
}



/**
 * Helper function to handle the camera shutter and image capture process.
 * @param context Context | The application or activity context
 * @param imageCapture ImageCapture | The CameraX use case for taking pictures
 * @param onImageCaptured (Bitmap) -> Unit | Lambda expression returned with the corrected Bitmap
 */
private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Bitmap) -> Unit
) {
    val mainExecutor = ContextCompat.getMainExecutor(context)

    imageCapture.takePicture(
        mainExecutor,
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                // Get the correct rotation degrees detected by the device hardware sensor
                val rotationDegrees = image.imageInfo.rotationDegrees

                // Convert raw bytes from ImageProxy planes into a usable Bitmap object
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                image.close() // Releasing memory buffer allocation immediately!

                if (originalBitmap != null) {
                    // Rotate the Bitmap source if the device orientation angle is different than 0
                    val correctedBitmap = if (rotationDegrees != 0) {
                        // Explicitly instantiate Android graphics matrix utility
                        val matrix = android.graphics.Matrix()

                        // Apply native rotation transformation matrix matching the sensor output
                        matrix.postRotate(rotationDegrees.toFloat())

                        Bitmap.createBitmap(
                            originalBitmap,
                            0,
                            0,
                            originalBitmap.width,
                            originalBitmap.height,
                            matrix,
                            true
                        )
                    } else {
                        originalBitmap
                    }

                    // Return the corrected orientation Bitmap object to the Composable trigger
                    onImageCaptured(correctedBitmap)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "Image capture error", exception)
            }
        }
    )
}
