package com.attrsense.android.test

import com.google.gson.Gson

@kotlinx.serialization.Serializable
data class Tooltips(
    val loading: String,
    val next: String,
    val previous: String,
    val walle: String,
    val walls: String
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}