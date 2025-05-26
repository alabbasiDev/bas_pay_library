package com.superstore.bas_pay.injections

import androidx.compose.ui.platform.UriHandler
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import com.superstore.bas_pay.models.BankyLitePayModel
import kotlinx.serialization.json.Json


class BankyLitePayInjection(
    private val onJsCallbackReceived: (BankyLitePayModel) -> Unit,
    private val callbackHandler: ((String) -> Unit) -> Unit,
) : IJsMessageHandler {
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {


        val result = BankyLitePayModel.fromJsonString(message.params)

        onJsCallbackReceived(result)

        callbackHandler(callback)
    }

    override fun methodName(): String {
        return "openBankyLitePayment"
    }

}
