@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.superstore.bas_pay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.superstore.bas_pay.models.BankyLitePayResponseModel
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.darwin.NSObjectProtocol
import platform.darwin.NSObject
import platform.darwin.sel_getUid
import platform.Foundation.*
import platform.UIKit.*


private const val BANKY_LITE_PAY_RESPONSE_NOTIFICATION = "BankyLiteResponseNotification"
private const val URL_KEY = "URL_KEY"

@Composable
actual fun BankyLitePay(txnToken: String, channel: String, onPaymentComplete: (BankyLitePayResponseModel) -> Unit ) {

    val bundleIdentifier : String = NSBundle.mainBundle.bundleIdentifier ?: "Unknown"
    val baseUrl : String = "bankylite:transfer?txnToken=$txnToken&channel=$channel&sourceAppId=$bundleIdentifier&schema=exampleschem:transfer"
    // ... (كود بناء الـ baseUrl يبقى كما هو) ...

    // استخدم DisposableEffect لربط دورة حياة الاستماع بدورة حياة الـ Composable
    LaunchedEffect(Unit) {
        // عندما يدخل الـ Composable إلى الشاشة، ابدأ الاستماع
//        myLogger("BankyLitePay : DisposableEffect Start")
        BankyLiteNotificationManager.startObserving(onPaymentComplete)
//        myLogger("BankyLitePay : DisposableEffect end")

//        // onDispose يتم استدعاؤه عندما يغادر الـ Composable الشاشة
//        onDispose {
//            myLogger("BankyLitePay : DisposableEffect onDispose start")
//            // توقف عن إرسال النتائج لتجنب تسرب البيانات إلى شاشات أخرى
//            BankyLiteNotificationManager.stopObserving()
//            myLogger("BankyLitePay : DisposableEffect onDispose end")
//        }
    }

    // هذا التأثير يبقى كما هو لفتح الرابط مرة واحدة
    LaunchedEffect(Unit){
//        myLogger("BankyLitePay : LaunchedEffect Start")
        try {
            // ... (نفس كود فتح الرابط) ...
            UIApplication.sharedApplication.openURL(NSURL(string = baseUrl), options = emptyMap<Any?, Any?>(), completionHandler = null)
        } catch (e: Exception){
            // ... (نفس كود معالجة الأخطاء) ...
        }
    }



//
//    val bundleIdentifier : String = NSBundle.mainBundle.bundleIdentifier ?: "Unknown"
//    /// let trxToken and channel is query parameters
////    val baseUrl : String = "bankylite:transfer?trxToken=$txnToken&channel=$channel&sourceAppId=$bundleIdentifier"
//    val baseUrl : String = "bankylite:transfer?txnToken=$txnToken&channel=$channel&sourceAppId=$bundleIdentifier&schema=exampleschem:transfer"
//
//    myLogger("BankyLitePay : From BankyLitePay baseUrl: $baseUrl")
//
//    myLogger("BankyLitePay : Start")
////
//    val observer =
//        remember {
//        object : NSObject(), NSObjectProtocol { // Inherit from NSObject and conform to NSObjectProtocol
//            // This method will be called when the notification is received
//            // It needs to be annotated with @ObjCAction for the selector to find it
//            @ObjCAction
//            fun handleBankyLiteResponse(notification: NSNotification) {
//                myLogger("BankyLitePay : handleBankyLiteResponse Start")
//                myLogger("BankyLitePay info : ${notification.userInfo}")
//                myLogger("BankyLitePay info key : ${notification.userInfo?.get(URL_KEY)}")
//                val urlString = notification.userInfo?.get(URL_KEY) as? String
//                myLogger("BankyLitePay info urlString : ${urlString}")
//                if (urlString != null) {
//                    val receivedUrl = NSURL(string = urlString)
//                    val components = NSURLComponents.componentsWithURL(receivedUrl, resolvingAgainstBaseURL = false)
//                    var status = false
//                    var result = "Payment Failed"
//                    var error: String? = null
//                    var code = "-1"
//
//                    components?.queryItems?.forEach { item ->
//                        val queryItem = item as platform.Foundation.NSURLQueryItem
//                        when (queryItem.name) {
//                            "status" -> status = queryItem.value?.toBooleanStrictOrNull() ?: false
//                            "result" -> result = queryItem.value ?: "Payment Failed"
//                            "error" -> error = queryItem.value
//                            "code" -> code = queryItem.value ?: "-1"
//                        }
//                    }
//                    onPaymentComplete(BankyLitePayResponseModel(status, result, error, code))
//                } else {
//                    myLogger("BankyLitePay Error : ${urlString}")
//                    onPaymentComplete(BankyLitePayResponseModel(status = false,
//                        result = "Failed to receive response from Banky Lite",
//                        error = "No URL received",
//                        code = "-1"))
//                }
//            }
//        }
//    }
//
//    myLogger("BankyLitePay : End observer")
//
//
//    LaunchedEffect(Unit){
//        myLogger("BankyLitePay : LaunchedEffect Start")
//        try {
//            myLogger("BankyLitePay : status ${UIApplication.sharedApplication.canOpenURL(NSURL(string = baseUrl))}")
//            myLogger("BankyLitePay : baseUrl $baseUrl")
////            if(UIApplication.sharedApplication.canOpenURL(NSURL(string = baseUrl))){
//                UIApplication.sharedApplication.openURL(NSURL(string = baseUrl), options = emptyMap<Any?, Any?>(), completionHandler = null)
////                UIApplication.sharedApplication.openURL(NSURL(string = baseUrl))
//                /// listen for response from com.ykb.bankylite
////            }else{
////                onPaymentComplete(BankyLitePayResponseModel(status = false,
////                    result = "Banky Lite app not installed" ,
////                    error = "Please install Banky Lite app to complete the payment",
////                    code = "-1"))
////            }
//        }catch (e: Exception){
//            myLogger("BankyLitePay : Exception ${e}")
//            onPaymentComplete(BankyLitePayResponseModel(status = false,
//                result = "Error occurred while processing payment",
//                error = e.message,
//                code = "-1"))
//        }
//
//    }
//    myLogger("BankyLitePay : LaunchedEffect End")
//
//    LaunchedEffect(Unit) {
//        myLogger("BankyLitePay : LaunchedEffect Start 2")
//        NSNotificationCenter.defaultCenter.addObserver(
//            observer = observer, // الكائن الذي يحتوي على الدالة
//            selector = NSSelectorFromString("handleBankyLiteResponse:"), // اسم الدالة التي سيتم استدعاؤها
//            name = "BankyLiteResponseNotification", // اسم الإشعار
//            `object` = null // يمكن تحديد المرسل، أو null للاستماع من أي مرسل
//        )
//        myLogger("BankyLitePay : LaunchedEffect End 2")
//    }
////    LaunchedEffect(Unit) {
////        NSNotificationCenter.defaultCenter.addObserver(
////            observer,
////            NSSelectorFromString("handleBankyLiteResponse:"),
////            BANKY_LITE_PAY_RESPONSE_NOTIFICATION, // اسم الإشعار
////            null
////        )
////        myLogger("BankyLitePay : observer registered")
////    }
////    DisposableEffect(Unit) {
////        myLogger("BankyLitePay : DisposableEffect Start 1")
////        NSNotificationCenter.defaultCenter.addObserver(
////            observer = observer,
////            selector = sel_getUid("handleBankyLiteResponse:"), // Correct way to get selector
////            name = BANKY_LITE_PAY_RESPONSE_NOTIFICATION,
////            `object` = null
////        )
////        myLogger("BankyLitePay : DisposableEffect Start 2")
////        onDispose {
////            myLogger("BankyLitePay : DisposableEffect End 1")
////            NSNotificationCenter.defaultCenter.removeObserver(
////                observer = observer,
////                name = BANKY_LITE_PAY_RESPONSE_NOTIFICATION,
////                `object` = null
////            )
////            myLogger("BankyLitePay : DisposableEffect End 2")
////        }
////    }
//    myLogger("BankyLitePay : End All")
}