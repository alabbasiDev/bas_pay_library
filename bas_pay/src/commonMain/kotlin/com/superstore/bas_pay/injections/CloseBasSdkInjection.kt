package com.superstore.bas_pay.injections

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import com.superstore.bas_pay.models.ResultStatusModel


class CloseBasSdkInjection(
    private val onJsCallbackReceived: (ResultStatusModel) -> Unit,
    private val callbackHandler: ((String) -> Unit) -> Unit,
) : IJsMessageHandler {
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        val result = ResultStatusModel.fromJsonString(message.params)

        onJsCallbackReceived(result)

        callbackHandler(callback)
    }

    override fun methodName(): String {
       return "closeBasSdk"
    }

}
