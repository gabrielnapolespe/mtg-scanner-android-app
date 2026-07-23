package com.zaziapps.mtg_scanner.ocr

import android.graphics.Bitmap
import kotlin.coroutines.resume
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.suspendCancellableCoroutine

class OcrViewModel : ViewModel() {

    /**
     * Extracts the first relevant text found in image
     * @param bitmap Bitmap | Image
     * @return String | Found text
     */
    suspend fun firstRelevantTextExtraction(bitmap: Bitmap): String {
        // Initialize OCR
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        // Prepare image
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        // Text recognition async operation
        return suspendCancellableCoroutine { continuation ->
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    val resultText = getFirstRelevantTextFromResult(visionText)
                    continuation.resume(resultText)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    continuation.resume("none")
                }
        }
    }

    /**
     * Gets the first relevant text, which is the first line of all the text found
     * @param result Text | Result to process
     * @return String | First relevant text
     */
    private fun getFirstRelevantTextFromResult(result: Text): String {
        return result.textBlocks[0].lines[0].text.trim()
    }
}