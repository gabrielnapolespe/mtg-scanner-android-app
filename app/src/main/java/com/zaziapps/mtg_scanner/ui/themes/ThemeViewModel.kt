package com.zaziapps.mtg_scanner.ui.themes

import androidx.lifecycle.ViewModel
import com.zaziapps.mtg_scanner.data.model.MagicCard
import com.zaziapps.mtg_scanner.ui.ScanScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel responsible for managing the application's thematic UI state and screen navigation flows.
 */
class ThemeViewModel : ViewModel() {

    // Mana background state (Defaults to UNCOLORED profile)
    private val _currentMana = MutableStateFlow(CardDetailBackground.UNCOLORED)
    val currentMana = _currentMana.asStateFlow()

    // Current navigation screen state (Defaults to the camera scanning view)
    private val _currentScreen = MutableStateFlow(ScanScreen.SCAN_CAM)
    val currentScreen = _currentScreen.asStateFlow()

    // State container holding the detected card data model object
    private val _selectedCard = MutableStateFlow<MagicCard?>(null)
    val selectedCard = _selectedCard.asStateFlow()

    /**
     * Updates the scanner results, switches the theme color, and navigates to the details view.
     * @param newMana CardDetailBackground | The target background color profile matching the card
     * @param card MagicCard | The complete scanned card data object model instance
     */
    fun updateScannerResult(newMana: CardDetailBackground, card: MagicCard) {
        _currentMana.value = newMana
        _selectedCard.value = card // Persist the full card data object structure
        _currentScreen.value = ScanScreen.CARD_DETAIL
    }

    /**
     * Resets the theme layout parameters and navigates back to the camera scanner interface.
     */
    fun returnToScanning() {
        _currentMana.value = CardDetailBackground.UNCOLORED
        _selectedCard.value = null // Purge cached card reference immediately on return
        _currentScreen.value = ScanScreen.SCAN_CAM
    }
}
