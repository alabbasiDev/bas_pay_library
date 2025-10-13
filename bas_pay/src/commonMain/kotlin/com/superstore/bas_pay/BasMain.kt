package com.superstore.bas_pay

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ShouldPauseCallback
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
import com.superstore.bas_pay.injections.WebLoggerInjection
import com.superstore.bas_pay.models.BankyLitePayModel
import com.superstore.bas_pay.models.BankyLitePayResponseModel
import com.superstore.bas_pay.models.InitBasSdkModel
import com.superstore.bas_pay.models.ResultStatusModel


@Composable
fun basSdk(
    trxToken: String,
    userIdentifier: String?,
    fullName: String?,
    language: String?,
    platform: String?,
    product: String?,
    onReturnDataToIOS: ((String) -> Unit)?,
    environment: String? = "prod",
) {
    fun baseUrl(): String {
        return when (environment) {
            "prod" -> "https://bas-pay.web.app"
            "dev" -> "https://bas-pay--dev-pb44x52j.web.app"
            else -> "https://bas-pay.web.app"
        }
    }


//    val testURL: String = "https://bas-sdk-web-dev.web.app"

    lateinit var webViewState: WebViewState

    lateinit var webViewNavigator: WebViewNavigator

    lateinit var jsBridge: WebViewJsBridge

    val initBasSdkModel: InitBasSdkModel = InitBasSdkModel(
        trxToken,
        userIdentifier,
        fullName,
        language,
        platform ?: "Native",
        osType(),
        product
    )


    var currentBankyLitePayData by remember { mutableStateOf<BankyLitePayModel?>(null) }

    var currentResultStatus by remember { mutableStateOf<BankyLitePayResponseModel?>(null) }

    var bankyLiteCallback by remember { mutableStateOf<((String) -> Unit)?>(null) }

    var closeBasSdkData by remember { mutableStateOf<ResultStatusModel?>(null) }

    var closeBasSdkCallback by remember { mutableStateOf<((String) -> Unit)?>(null) }

    webViewState = rememberWebViewState(baseUrl())

    webViewNavigator = rememberWebViewNavigator()

    jsBridge = rememberWebViewJsBridge(webViewNavigator)

    fun callbackForBankyLitePay(currentResultStatus : String) : Unit {
        bankyLiteCallback?.invoke(currentResultStatus)
    }

    fun callbackForCloseBasSdk(closeBasSdkData : String) : Unit {
//        myLogger("callbackForCloseBasSdk closeBasSdkData: $closeBasSdkData")
        closeBasSdkCallback?.invoke(closeBasSdkData)
    }

    LaunchedEffect(Unit) {
//        withContext(Dispatchers.Main){
//            jsBridge.apply {
//
//            }
            jsBridge.register(InitBasSdkInjection(initBasSdkModel))
            jsBridge.register(CloseBasSdkInjection(onJsCallbackReceived = {data ->  closeBasSdkData = data},
                callbackHandler = {callbackResult -> closeBasSdkCallback = callbackResult
//            myLogger("closeBasSdkCallback result: $closeBasSdkCallback")
//            myLogger("callbackResult result: $callbackResult")
                }))
            jsBridge.register(BankyLitePayInjection(onJsCallbackReceived = { data ->
                currentBankyLitePayData = data
                myLogger("currentBankyLitePayData: $currentBankyLitePayData")
                myLogger("currentBankyLitePayData Data: $data")
            }, callbackHandler = { callbackResult ->
                bankyLiteCallback = callbackResult
            }))

            jsBridge.register(WebLoggerInjection())
//        }
    }

    LaunchedEffect(currentResultStatus) {
        if(currentResultStatus != null){
            callbackForBankyLitePay(currentResultStatus!!.toJson())
            currentResultStatus = null
        }
    }

    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        supportZoom = false

        androidWebSettings.apply {
            useWideViewPort = true
            domStorageEnabled = true
        }

        iOSWebSettings.apply {

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



    currentBankyLitePayData.apply {
        if (this != null) {
            BankyLitePay(this.txnToken, this.channel!!){
                result ->
                currentResultStatus = result
            }
            currentBankyLitePayData = null
        }
    }

    closeBasSdkData.apply {
        if(osType() == "Android"){
            if (this != null) {
                val closeBasData = closeBasSdkData!!.toJsonString()
                closeBasSdk(closeBasData)
                callbackForCloseBasSdk(closeBasData)
                closeBasSdkData = null
            }
        }else if (osType() == "IOS"){
            if (this != null) {
                onReturnDataToIOS?.invoke(closeBasSdkData!!.toJsonString())
                closeBasSdkData = null
            }
        }
    }


}