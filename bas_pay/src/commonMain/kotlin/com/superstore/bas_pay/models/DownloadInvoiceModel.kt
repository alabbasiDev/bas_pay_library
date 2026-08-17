package com.superstore.bas_pay.models

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object DownloadInvoiceModelFields {
    const val fileName: String = "fileName"
    const val mimeType: String = "mimeType"
    const val base64: String = "base64"
    const val status: String = "status"
    const val error: String = "error"
}

@Serializable
data class DownloadInvoiceModel(
    val fileName: String,
    val mimeType: String = "application/pdf",
    val base64: String,
) {
    fun resolvedFileName(): String {
        val trimmed = fileName.trim().ifEmpty { "invoice.pdf" }
        val sanitized = trimmed.replace(Regex("[^A-Za-z0-9._-]"), "_")
        return if (sanitized.endsWith(".pdf", ignoreCase = true)) {
            sanitized
        } else {
            "$sanitized.pdf"
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decodePdfBytes(): ByteArray {
        return Base64.Default.decode(base64)
    }

    companion object {
        fun fromJsonString(jsonString: String): DownloadInvoiceModel {
            val json = Json { ignoreUnknownKeys = true }
            return json.decodeFromString<DownloadInvoiceModel>(jsonString)
        }
    }
}

@Serializable
data class DownloadInvoiceResult(
    val status: Boolean,
    val error: String? = null,
) {
    fun toJsonString(): String {
        val json = Json { encodeDefaults = true }
        return json.encodeToString(this)
    }
}
