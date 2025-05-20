package com.superstore.bas_pay.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object BankyLitePayModelFields {
    const val txnToken : String = "txnToken"
    const val channel : String = "channel"
    // Add other fields if needed
}

@Serializable
data class BankyLitePayModel(
    val txnToken: String,
    val channel: String? = null,
) {

    companion object {

        fun fromJsonString(jsonString: String): BankyLitePayModel {
            val json = Json
            val data = json.decodeFromString<BankyLitePayModel>(jsonString)
            return data
        }

    }
}