package com.zaziapps.mtg_scanner.scryfall

import androidx.lifecycle.ViewModel
import com.zaziapps.mtg_scanner.data.model.MagicCard
import com.zaziapps.mtg_scanner.network.ScryfallRepository
import com.zaziapps.mtg_scanner.ui.themes.CardDetailBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScryfallViewModel : ViewModel() {
    private val repository = ScryfallRepository()
    private val manas = setOf('W', 'U', 'B', 'R', 'G')

    /**
     * Searches a card by name in the langInput specified language and returns the found card in the langOutput specified language
     * @param name String | Name of the card
     * @param langInput String | Language of the input text (cardName) - Default ='en'
     * @langOutput langInput String | Language of the output result - Default ='en'
     * @return MagicCard if a result is found | @null if nothing is found or an error occurs
     */
    suspend fun searchCardByName(name: String, langInput: String = "en", langOutput: String = "en"): MagicCard? {
        if (name.isBlank()) return null

        // Execute the code and
        return withContext(Dispatchers.IO) {
            val originalCard = repository.searchByNameAndLanguage(name, langInput)

            // If langInput and LangOutput are equal, no need for extra search in ScryfallBD
            if (langInput == langOutput) {
                return@withContext originalCard
            }
            val traducedCard = repository.searchByOracleIdAndLanguage(originalCard?.oracleId , langOutput)

            traducedCard
        }
    }

    /**
     * Extracts main color of the card given
     * @param card MagicCard | Card object
     * @return CardDetailBackground
     */
    fun extractMainColor(card: MagicCard): CardDetailBackground {

        val manaCost = card.manaCost.toString()

        // Search by first char in the string manaCost
        val mainColor = manaCost.firstOrNull { it in manas }

        // Define background
        val background = when (mainColor) {
            'W' -> CardDetailBackground.WHITE
            'U' -> CardDetailBackground.BLUE
            'B' -> CardDetailBackground.BLACK
            'R' -> CardDetailBackground.RED
            'G' -> CardDetailBackground.GREEN
            else -> CardDetailBackground.UNCOLORED // Uncolored by default
        }

        return background
    }
}