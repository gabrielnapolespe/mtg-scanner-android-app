package com.zaziapps.mtg_scanner.data.model

import com.google.gson.annotations.SerializedName

data class MagicCard(
    @SerializedName("id") val id: String,
    @SerializedName("oracle_id") val oracleId: String,
    @SerializedName("name") val name: String,
    @SerializedName("printed_name") val printedName: String?,
    @SerializedName("mana_cost") val manaCost: String?,
    @SerializedName("type_line") val typeLine: String?,
    @SerializedName("printed_type_line") val printedTypeLine: String?,
    @SerializedName("image_uris") val imageUris: ImageUris?,
    @SerializedName("set") val set: String?,
    @SerializedName("rarity") val rarity: String?,
    @SerializedName("oracle_text") val oracleText: String?,
    @SerializedName("printed_text") val printedText: String?
)
