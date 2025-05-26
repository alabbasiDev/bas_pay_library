package com.superstore.bas_pay.models


import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


object ResultStatusModelFields {
    const val status : String = "status"
    const val message : String = "message"
    const val result : String = "result"
    const val code : String = "code"
}

@Serializable
data class ResultStatusModel(
    val status: Boolean,
    val message: String,
    val result: String? = null,
    val code: Int? = null
) {

    fun toJsonMap(): Map<String, Any> {
        val data = mutableMapOf<String, Any>()
        data[ResultStatusModelFields.status] = status
//        if (message!= null) {
            data[ResultStatusModelFields.message] = message
//        }
        if (result!= null) {
            data[ResultStatusModelFields.result] = result
        }
        if (code!= null) {
            data[ResultStatusModelFields.code] = code
        }
        return data
    }

    fun toJsonString(): String {
        val json = Json
//        val map = toJsonMap()
        return json.encodeToString(this)
    }



    companion object {

        /// convert query string to toJsonString
        fun fromQueryStringToJsonString(queryString: String): String {
            val map = mutableMapOf<String, Any>()
            queryString.split("https://www.bas.com/").last().split("&").forEach {
                val keyValue = it.split("=")
                map[keyValue[0]] = keyValue[1].replace("%20", " ")
            }

            val result = ResultStatusModel(
                /// convert from String to Boolean
//                status = map[ResultStatusModelFields.status] as String == "true",
                status = (map[ResultStatusModelFields.status] as String?)?.toBoolean() ?: false,
                message = map[ResultStatusModelFields.message] as String,
                result = map[ResultStatusModelFields.result] as String?,
                code = (map[ResultStatusModelFields.code] as String? )?.toIntOrNull()
            )

            val json = Json
            return json.encodeToString(result)
        }


        /// convert json to query string
        fun fromJsonStringToQueryString(jsonString: String): String {
            val json = Json
            val map = json.decodeFromString<ResultStatusModel>(jsonString).toJsonMap()
            return map.map { "${it.key}=${it.value}" }.joinToString("&")
        }

        fun fromJsonString(jsonString: String): ResultStatusModel {
            val json = Json
            return json.decodeFromString<ResultStatusModel>(jsonString)
        }



    }



}