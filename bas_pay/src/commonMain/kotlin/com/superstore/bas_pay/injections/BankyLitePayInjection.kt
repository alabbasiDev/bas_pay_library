package com.superstore.bas_pay.injections

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import com.superstore.bas_pay.models.BankyLitePayModel
import kotlinx.serialization.json.Json


class BankyLitePayInjection(
    private val onJsCallbackReceived: (BankyLitePayModel) -> Unit
) : IJsMessageHandler {
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {

        val result = BankyLitePayModel.fromJsonString(message.params)

        onJsCallbackReceived(result)

//        val query = ResultStatusModel.fromJsonStringToQueryString(message.params)

//        navigator?.loadUrl("https://www.bas.com/${query}")

//        callback(query)
    }

    override fun methodName(): String {
        return "openBankyLitePayment"
    }

}
