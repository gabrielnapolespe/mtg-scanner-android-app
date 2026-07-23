package com.zaziapps.mtg_scanner.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zaziapps.mtg_scanner.ui.themes.Black
import com.zaziapps.mtg_scanner.ui.themes.DarkRed
import com.zaziapps.mtg_scanner.ui.themes.Gold
import com.zaziapps.mtg_scanner.ui.themes.LightBrown

/**
 * Loading Dialog.
 * @param isLoading Boolean | Controls the visibility of the dialog
 * @param modifier Modifier | View modifier
 */
@Composable
fun LoadingDialog(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Dialog(
            onDismissRequest = { /* Blocked to prevent user interruptions */ },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                modifier = modifier
                    .size(110.dp) // Reduced size to perfectly fit the indicator alone
                    .background(Black, shape = RoundedCornerShape(16.dp)) // Mystic background
                    .border(BorderStroke(2.dp, Gold), shape = RoundedCornerShape(16.dp)), // Gold border
                contentAlignment = Alignment.Center
            ) {
                // Centered loading indicator
                CircularProgressIndicator(
                    color = DarkRed, // DarkRed
                    trackColor = LightBrown.copy(alpha = 0.3f),
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}
