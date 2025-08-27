package com.superstore.bas_pay.models
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object InitBasSdkModelFields {
    const val trxToken : String = "trxToken"
    const val userIdentifier : String = "userIdentifier"
    const val fullName : String = "fullName"
    const val language : String = "language"
    const val platform : String = "platform"
    const val osType : String = "osType"
    const val product : String = "product"
    // Add other fields if needed
}


@Serializable // Add this annotation
data class InitBasSdkModel(
    val trxToken: String,
    val userIdentifier: String? = null,
    val fullName: String? = null,
    val language: String? = null,
    val platform: String,
    val osType: String,
    val product: String? = null,
) {


    fun toJsonMap(): Map<String, String> {
        val data = mutableMapOf<String, String>()
        data[InitBasSdkModelFields.trxToken] = this.trxToken
        if(this.userIdentifier != null)
        data[InitBasSdkModelFields.userIdentifier] = this.userIdentifier
        if(this.fullName != null)
        data[InitBasSdkModelFields.fullName] = this.fullName
        if(this.language != null)
        data[InitBasSdkModelFields.language] = this.language

        data[InitBasSdkModelFields.platform] = this.platform
        data[InitBasSdkModelFields.osType] = this.osType
        if(this.product != null)
        data[InitBasSdkModelFields.product] = this.product
        return data
    }


    fun toJsonString(): String {
        val json = Json
        return json.encodeToString(toJsonMap())
    }


}