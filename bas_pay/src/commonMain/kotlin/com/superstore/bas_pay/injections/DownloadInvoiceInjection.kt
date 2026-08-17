package com.superstore.bas_pay.injections

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import com.superstore.bas_pay.models.DownloadInvoiceModel
import com.superstore.bas_pay.models.DownloadInvoiceResult

class DownloadInvoiceInjection(
    private val onJsCallbackReceived: (DownloadInvoiceModel) -> Unit,
    private val callbackHandler: ((String) -> Unit) -> Unit,
) : IJsMessageHandler {
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        try {
            val request = DownloadInvoiceModel.fromJsonString(message.params)
            callbackHandler(callback)
            onJsCallbackReceived(request)
        } catch (error: Exception) {
            callback(
                DownloadInvoiceResult(
                    status = false,
                    error = error.message,
                ).toJsonString()
            )
        }
    }

    override fun methodName(): String {
        return "downloadInvoice"
    }
}
