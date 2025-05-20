package com.superstore.bas_pay.injections

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import com.superstore.bas_pay.models.InitBasSdkModel

class InitBasSdkInjection(
    val initBasSdkModel: InitBasSdkModel
) : IJsMessageHandler {
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        callback(initBasSdkModel.toJsonString())
    }

    override fun methodName(): String {
        return  "initBasSdk"
    }
}