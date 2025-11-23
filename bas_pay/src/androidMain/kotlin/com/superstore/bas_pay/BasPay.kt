package com.superstore.bas_pay

import android.app.Activity
import android.content.Intent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Public entry point for starting the Bas Pay SDK flow.
 * Provides a single function usable from Kotlin, Java, and Flutter (via platform channel).
 */
object BasPay {
    const val EXTRA_MESSAGE_JSON: String = "message"
    const val EXTRA_RESULT: String = "result"
    const val DEFAULT_REQUEST_CODE: Int = 1234

    /**
     * Start Bas Pay SDK UI flow.
     *
     * Java usage:
     * BasPay.start(this, "trxToken", null, null, null, null, null, "prod", BasPay.DEFAULT_REQUEST_CODE);
     *
     * Kotlin usage:
     * BasPay.start(activity, trxToken = "...", userIdentifier = "user123")
     *
     * Flutter plugin usage (inside onMethodCall):
     * BasPay.start(activity, trxToken, userIdentifier, fullName, language, platform, product, environment, requestCode)
     */
    @JvmStatic
    @JvmOverloads
    fun start(
        activity: Activity,
        trxToken: String,
        userIdentifier: String? = null,
        fullName: String? = null,
        language: String? = null,
        platform: String? = null,
        product: String? = null,
        environment: String? = "prod",
        requestCode: Int = DEFAULT_REQUEST_CODE
    ) {
        val payloadMap = buildMap<String, String> {
            put("trxToken", trxToken)
            platform?.let { put("platform", it) } ?: put("platform", "Native")
            environment?.let { put("environment", it) }
            userIdentifier?.let { put("userIdentifier", it) }
            fullName?.let { put("fullName", it) }
            language?.let { put("language", it) }
            product?.let { put("product", it) }
        }
        val jsonPayload = Json.encodeToString(payloadMap)
        val intent = Intent(activity, BasActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE_JSON, jsonPayload)
        }
        activity.startActivityForResult(intent, requestCode)
    }

    /** Extract result string from onActivityResult intent */
    @JvmStatic
    fun getResult(data: Intent?): String? = data?.getStringExtra(EXTRA_RESULT)
}

