package com.zaziapps.mtg_scanner.ui.components

import com.zaziapps.mtg_scanner.R
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.zaziapps.mtg_scanner.ui.themes.Beige
import com.zaziapps.mtg_scanner.ui.themes.Black
import com.zaziapps.mtg_scanner.ui.themes.Brown
import com.zaziapps.mtg_scanner.ui.themes.Gold
import com.zaziapps.mtg_scanner.ui.themes.LightBrown
import com.zaziapps.mtg_scanner.ui.themes.DarkRed
import com.zaziapps.mtg_scanner.ui.themes.Red

/**
 * Scan Confirmation Dialog to preview and confirm the captured card image.
 * @param showDialog Boolean | Controls the visibility of the dialog popup
 * @param capturedBitmap Bitmap? | The captured image to preview
 * @param onDismiss () -> Unit | Lambda expression triggered when closing or discarding the dialog
 * @param onScanClick (Bitmap) -> Unit | Lambda expression triggered when confirming the scan process
 */
@Composable
fun ScanConfirmationDialog(
    showDialog: Boolean,
    capturedBitmap: Bitmap?,
    onDismiss: () -> Unit,
    onScanClick: (Bitmap) -> Unit
) {
    if (showDialog && capturedBitmap != null) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            onDismissRequest = onDismiss
        ) {
            // Main container: Grimor/mystic stone fantasy theme styling
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .border(BorderStroke(2.dp, LightBrown), shape = RoundedCornerShape(24.dp)), // Old leather style border
                shape = RoundedCornerShape(24.dp),
                color = Black, // Dark mystic background matching LoadingDialog
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    // Image container (5:7)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .aspectRatio(5f / 7f)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                width = 2.dp,
                                color = Gold.copy(alpha = 0.6f), // Subtle gold border for the preview photo
                                shape = RoundedCornerShape(14.dp)
                            )
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = capturedBitmap.asImageBitmap(),
                            contentDescription = stringResource(id = R.string.card_preview_alt_text),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Horizontal action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp), // Balanced spacing between items
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cancel button (Secondary stone/wood design element)
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, Brown),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Beige // Parchment paper tone
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.discard_button_text).uppercase(),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        // Scan button
                        Button(
                            onClick = {
                                onScanClick(capturedBitmap)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkRed,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.5.dp, Red.copy(alpha = 0.5f)), // Subtle magical glow outline
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.scan_button_text).uppercase(),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
