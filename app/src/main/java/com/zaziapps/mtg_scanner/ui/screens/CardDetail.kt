package com.zaziapps.mtg_scanner.ui.screens

import com.zaziapps.mtg_scanner.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.zaziapps.mtg_scanner.data.model.MagicCard
import com.zaziapps.mtg_scanner.ui.themes.CardDetailBackground

/**
 * Background layout wrapper optimized for Magic: The Gathering thematic colors.
 * @param manaColor CardDetailBackground | Theme properties matching the card mana profile
 * @param content @Composable () -> Unit | Nested visual components to render inside the wrapper
 */
@Composable
fun BackgroundMTG(
    manaColor: CardDetailBackground,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(manaColor.gradient) // Applies the mystic color gradient background
    ) {
        // Content layout container layer
        content()
    }
}

/**
 * Main detail screen to render information and high-quality artwork of the scanned MTG card.
 * @param card MagicCard? | The scanned card data model structure
 * @param selectedMana CardDetailBackground | Theme colors determined by the card properties
 * @param onBackButtonClick () -> Unit | Action lambda expression to return to the scanner interface
 */
@Composable
fun MainScreenContainer(
    card: MagicCard?,
    selectedMana: CardDetailBackground,
    onBackButtonClick: () -> Unit
) {
    val context = LocalContext.current

    BackgroundMTG(manaColor = selectedMana) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Container for the localized external card artwork image
            card?.imageUris?.normal?.let { imageUrl ->
                // Building a secure and compliant network request structure targeting Scryfall API guidelines
                val secureRequest = remember(imageUrl) {
                    ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .setHeader("User-Agent", "MTGScannerApp/1.0 (com.example.mtg_scanner)")
                        .setHeader("Accept", "image/webp,image/png,image/*;q=0.8")
                        .build()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = secureRequest,
                        contentDescription = stringResource(id = R.string.card_ilustration_alt_text),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxHeight()
                            .heightIn(max = 500.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .shadow(8.dp, RoundedCornerShape(12.dp)),
                        loading = {
                            CircularProgressIndicator(
                                color = selectedMana.textColor,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(0.71f)
                                    .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.error_download_art_text),
                                    color = selectedMana.textColor.copy(alpha = 0.6f),
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    )
                }
            }

            // Scryfall API data identifier logs at the bottom section layout boundary
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Scryfall ID: ${card?.id ?: "---"}",
                    color = selectedMana.textColor.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp
                )
            }

            // Static action button persistently locked at the bottom layout container anchor
            Button(
                onClick = onBackButtonClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = selectedMana.textColor,
                    contentColor = if (selectedMana == CardDetailBackground.WHITE) Color.Black else Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.scan_button_text),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}
