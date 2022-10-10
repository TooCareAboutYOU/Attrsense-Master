package com.attrsense.android.model

import com.google.gson.Gson

@kotlinx.serialization.Serializable
data class Image(
    val bot: Int,
    val copyright: String,
    val copyrightlink: String,
    val drk: Int,
    val enddate: String,
    val fullstartdate: String,
    val hs: List<String>,
    val hsh: String,
    val quiz: String,
    val startdate: String,
    val title: String,
    val top: Int,
    val url: String,
    val urlbase: String,
    val wp: Boolean
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}