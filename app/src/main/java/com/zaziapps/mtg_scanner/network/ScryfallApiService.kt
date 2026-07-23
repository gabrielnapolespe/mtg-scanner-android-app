package com.zaziapps.mtg_scanner.network

import com.zaziapps.mtg_scanner.data.model.ScryfallResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ScryfallApiService {
    @GET("cards/search")
    suspend fun searchCard(
        @Query("q") query: String
    ): Response<ScryfallResponse>
}