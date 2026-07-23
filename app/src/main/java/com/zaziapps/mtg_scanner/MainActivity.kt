package com.zaziapps.mtg_scanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zaziapps.mtg_scanner.ui.screens.CameraView
import com.zaziapps.mtg_scanner.ui.themes.MTGScannerTheme
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.zaziapps.mtg_scanner.ui.ScanScreen
import com.zaziapps.mtg_scanner.ui.screens.MainScreenContainer
import com.zaziapps.mtg_scanner.ui.themes.ThemeViewModel

class MainActivity : ComponentActivity() {

    // 1. Initialize the MTG theme controller
    private val themeViewModel by viewModels<ThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MTGScannerTheme {
                // Inside the setContent of your MainActivity.kt:
                val activeMana by themeViewModel.currentMana.collectAsState()
                val activeScreen by themeViewModel.currentScreen.collectAsState()
                val activeCard by themeViewModel.selectedCard.collectAsState()

                Crossfade(
                    targetState = activeScreen,
                    animationSpec = tween(600),
                    label = "AppNavigation"
                ) { screen ->
                    when (screen) {
                        ScanScreen.SCAN_CAM -> {
                            CameraView(
                                onManaDetected = { detectedMana, card ->
                                    themeViewModel.updateScannerResult(detectedMana, card)
                                }
                            )
                        }
                        ScanScreen.CARD_DETAIL -> {
                            MainScreenContainer(
                                card = activeCard,
                                selectedMana = activeMana,
                                onBackButtonClick = {
                                    themeViewModel.returnToScanning()
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}
