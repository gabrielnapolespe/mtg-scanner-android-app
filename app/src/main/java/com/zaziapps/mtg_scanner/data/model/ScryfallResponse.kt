package com.zaziapps.mtg_scanner.data.model

import com.google.gson.annotations.SerializedName

data class ScryfallResponse(
    @SerializedName("data") val data: List<MagicCard>
)