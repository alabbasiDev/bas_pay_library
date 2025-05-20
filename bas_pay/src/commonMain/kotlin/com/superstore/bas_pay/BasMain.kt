package com.superstore.bas_pay

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import com.superstore.bas_pay.injections.BankyLitePayInjection
import com.superstore.bas_pay.injections.CloseBasSdkInjection
import com.superstore.bas_pay.injections.InitBasSdkInjection
import com.superstore.bas_pay.models.BankyLitePayModel
import com.superstore.bas_pay.models.InitBasSdkModel
import com.superstore.bas_pay.models.ResultStatusModel


@Composable
fun basSdk(trxToken: String, userIdentifier: String?, fullName: String?, language: String?, platform: String?){

    val testURL: String = "https://bas-sdk-web-dev.web.app"

    lateinit var webViewState: WebViewState

    lateinit var webViewNavigator: WebViewNavigator

    lateinit var jsBridge: WebViewJsBridge

    val initBasSdkModel:InitBasSdkModel = InitBasSdkModel(
        trxToken,
        userIdentifier,
        fullName,
        language,
        platform ?: "Native",
        osType())

    var currentBankyLitePayData by remember { mutableStateOf<BankyLitePayModel?>(null) }

    webViewState = rememberWebViewState(testURL)

    webViewNavigator = rememberWebViewNavigator()

    jsBridge = rememberWebViewJsBridge(webViewNavigator)


    LaunchedEffect(Unit) {
        jsBridge.register(InitBasSdkInjection(initBasSdkModel))
        jsBridge.register(CloseBasSdkInjection())
        jsBridge.register(BankyLitePayInjection({
            data ->
            currentBankyLitePayData = data
//            bankyLitePay(data.txnToken, data.channel)
        }))
    }

    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        supportZoom = false

        androidWebSettings.apply {
            useWideViewPort = true
            domStorageEnabled = true
        }
    }

    webViewState.apply {
        if(webViewState.lastLoadedUrl?.contains("https://www.bas.com/") == true){
            closeBasSdk(ResultStatusModel.fromQueryStringToJsonString(webViewState.lastLoadedUrl!!))
        }
    }

    WebView(
        webViewState,
        webViewJsBridge = jsBridge,
        navigator = webViewNavigator,
        modifier = Modifier.fillMaxSize()
              .systemBarsPadding()
            .imePadding()
    )

    if(currentBankyLitePayData!= null){

        bankyLitePay(currentBankyLitePayData!!.txnToken, currentBankyLitePayData!!.channel!!)
    }
}

//@Composable
//fun bankyLitePayFunction(txnToken: String, channel: String): String {
//    return bankyLitePay(txnToken, channel)
//}

//class BasMain {
//
//    private val testURL: String = "https://bas-sdk-web-dev.web.app"
//
//    private lateinit var webViewState: WebViewState
//
//    private lateinit var webViewNavigator: WebViewNavigator
//
//    private lateinit var jsBridge: WebViewJsBridge
//
//
////    init {
////
////    }
//
//    @Composable
//    fun basSdk(trxToken: String, userIdentifier: String?, fullName: String?, language: String?){
//
//        val initBasSdkModel:InitBasSdkModel = InitBasSdkModel(trxToken, userIdentifier, fullName, language)
//
//        webViewState = rememberWebViewState(testURL)
//
//        webViewNavigator = rememberWebViewNavigator()
//
//        jsBridge = rememberWebViewJsBridge(webViewNavigator)
//
//
//        LaunchedEffect(Unit) {
//            jsBridge.register(InitBasSdkInjection(initBasSdkModel))
//        }
//
//        webViewState.webSettings.apply {
//            isJavaScriptEnabled = true
//            androidWebSettings.apply {
//                useWideViewPort = true
//                domStorageEnabled = true
//            }
//        }
//
//        WebView(
//            webViewState,
//            webViewJsBridge = jsBridge,
//            navigator = webViewNavigator,
//            modifier = Modifier.fillMaxSize()
//            )
//
//    }
//
//}