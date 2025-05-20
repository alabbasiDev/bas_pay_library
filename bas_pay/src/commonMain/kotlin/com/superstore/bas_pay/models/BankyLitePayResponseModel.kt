package com.superstore.bas_pay.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

object BankyLitePayResponseModelFields {
    const val STATUS = "status"
    const val RESULT = "result"
    const val ERROR = "error"
    const val CODE = "code"
}


@Serializable
class BankyLitePayResponseModel(
    val status: Boolean,
    val result: String?,
    val error: String?,
    val code: String?
){

    companion object {
//        fun fromJson(json: String): BankyLitePayResponseModel {
//            val jsonObject = JSONObject(json)
//            return BankyLitePayResponseModel(
//                status = jsonObject.getBoolean(BankyLitePayResponseModelFields.STATUS),
//                result = jsonObject.optString(BankyLitePayResponseModelFields.RESULT),
//                error = jsonObject.optString(BankyLitePayResponseModelFields.ERROR),
//                code = jsonObject.optString(BankyLitePayResponseModelFields.CODE)
//            )
//        }
    }
}