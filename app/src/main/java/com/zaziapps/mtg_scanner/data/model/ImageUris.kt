package com.zaziapps.mtg_scanner.data.model

import com.google.gson.annotations.SerializedName

data class ImageUris(
    @SerializedName("normal") val normal: String?
)
