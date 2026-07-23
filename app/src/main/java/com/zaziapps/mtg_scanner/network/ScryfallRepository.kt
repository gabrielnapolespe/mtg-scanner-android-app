package com.zaziapps.mtg_scanner.network

import com.zaziapps.mtg_scanner.data.model.MagicCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScryfallRepository {

    private val apiService: ScryfallApiService

    init {
        // 1. Configuration of OkHttpClient with required heads for Scryfall
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "MagicScanApp/1.0 (gabriel.napoles.pe@gmail.com)") // TODO: USE USER MAIL VIA Google Sign-In (Credential Manager)/Firebase Authentication
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

        // 2. Retrofit construction and association with OkHttpClient created object
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.scryfall.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ScryfallApiService::class.java)
    }

    /**
     * Search a card by name in the specified language
     * @param cardName String | Name of the card
     * @param language String | Language of the input text (cardName) - Default ='en'
     * @return MagicCard if a result is found | @null if nothing is found or an error occurs
     */
    suspend fun searchByNameAndLanguage(cardName: String, language: String = "en"): MagicCard? {
        return withContext(Dispatchers.IO) {
            try {
                // Search card information by name (cardName) in the specified language
                val query = "lang:$language !\"$cardName\""
                val response = apiService.searchCard(query)

                // If an error is returned in the API response, return null
                if (!response.isSuccessful || response.body() == null) {
                    logError(response)
                    return@withContext null
                }

                val cardsArray = response.body()!!.data
                // If no match was found, return null
                if (cardsArray.isEmpty()) return@withContext null

                // Return information data as MagicCard object
                return@withContext cardsArray[0]

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Search by oracle id in the specified language
     * @param oracleId String | Oracle BD identifier
     * @param language String | Language of the desired card - Default ='en'
     * @return MagicCard if a result is found | @null if nothing is found or an error occurs
     */
    suspend fun searchByOracleIdAndLanguage(oracleId: String?, language: String = "en"): MagicCard? {
        return withContext(Dispatchers.IO) {
            try {

                // Search card by orcale id in specified language
                val query = "oracle_id:$oracleId lang:$language"
                val response = apiService.searchCard(query)

                // If an error is returned in the API response, return null
                if (response.isSuccessful && response.body() != null) {
                    val cardsArray = response.body()!!.data
                    if (cardsArray.isEmpty()) return@withContext null
                    return@withContext cardsArray[0]
                } else {
                    // If card not exists
                    android.util.Log.w("Scryfall", "Found card : $language")
                    logError(response)
                    null
                }

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    private fun logError(response: retrofit2.Response<*>) {
        val errorJson = response.errorBody()?.string()
        android.util.Log.e("ScryfallError", "Code HTTP: ${response.code()}")
        android.util.Log.e("ScryfallError", "Error details: $errorJson")
    }
}