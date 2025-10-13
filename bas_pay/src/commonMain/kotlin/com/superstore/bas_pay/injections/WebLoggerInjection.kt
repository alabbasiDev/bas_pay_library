package com.superstore.bas_pay.injections

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import com.superstore.bas_pay.myLogger

class WebLoggerInjection() : IJsMessageHandler {
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {

        myLogger("Logger From JS : ${message.params}")

        callback("Logger From JS : ${message.params}")
    }

    override fun methodName(): String {
        return "WebLoggerInjection"
    }

}
