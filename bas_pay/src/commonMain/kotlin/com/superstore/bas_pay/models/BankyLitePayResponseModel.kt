package com.superstore.bas_pay.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json

object BankyLitePayResponseModelFields {
    const val STATUS = "status"
    const val RESULT = "result"
    const val ERROR = "error"
    const val CODE = "code"
}


@Serializable
data class BankyLitePayResponseModel(
    val status: Boolean,
    val result: String? = null,
    val error: String? = null,
    val code: String? = null
) {

    /// to Map
    fun toMap(): Map<String, String?> {
        val data = mutableMapOf<String, String>()
        data[BankyLitePayResponseModelFields.STATUS] = this.status.toString()
        if(this.result != null)
        data[BankyLitePayResponseModelFields.RESULT] = this.result
        if(this.error != null)
        data[BankyLitePayResponseModelFields.ERROR] = this.error
        if(this.code != null)
        data[BankyLitePayResponseModelFields.CODE] = this.code
        return data
    }

    /// to json map
    fun toJson(): String {
        val json = Json
        return json.encodeToString(toMap())
    }


}