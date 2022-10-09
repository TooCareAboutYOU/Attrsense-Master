package com.attrsense.android.model

import kotlinx.serialization.Serializable

/**
 * @param token: 用于登录的token
 * @param refresh_token: 用于刷新token的token
 */
@Serializable
data class LoginBean(
    val token: String,
    val refresh_token: String
)
