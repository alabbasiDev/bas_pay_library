package com.superstore.bas_pay

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.ui.platform.ComposeView
import kotlinx.serialization.json.Json

/** Internal hosting Activity for the Bas Pay SDK Compose content. */
class BasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Set transparent background before super to reduce flash
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)

        val json = Json
        val messageJsonString: String? = intent?.getStringExtra(BasPay.EXTRA_MESSAGE_JSON)
        if (messageJsonString == null) {
            finishWithError("Missing message JSON")
            return
        }

        val messageMap: Map<String, String?> = try {
            json.decodeFromString<Map<String, String?>>(messageJsonString)
        } catch (e: Exception) {
            finishWithError("Invalid message JSON: ${e.message}")
            return
        }

        setContentView(
            ComposeView(this).apply {
                layoutParams = LinearLayoutCompat.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                setContent {
                    basSdk(
                        trxToken = messageMap["trxToken"] ?: "",
                        userIdentifier = messageMap["userIdentifier"],
                        fullName = messageMap["fullName"],
                        language = messageMap["language"],
                        platform = messageMap["platform"],
                        product = messageMap["product"],
                        onReturnDataToIOS = null,
                        environment = messageMap["environment"]
                    )
                }
            }
        )
    }

    private fun finishWithError(error: String) {
        val resultIntent = Intent().apply { putExtra(BasPay.EXTRA_RESULT, error) }
        setResult(Activity.RESULT_CANCELED, resultIntent)
        finish()
    }
}
