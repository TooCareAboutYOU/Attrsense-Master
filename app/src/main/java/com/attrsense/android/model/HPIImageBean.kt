package com.attrsense.android.model

import com.google.gson.Gson

@kotlinx.serialization.Serializable
data class HPIImageBean(
    val images: List<Image>,
    val tooltips: Tooltips
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}