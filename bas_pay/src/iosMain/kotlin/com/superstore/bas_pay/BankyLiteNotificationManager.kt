@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.superstore.bas_pay

import com.superstore.bas_pay.models.BankyLitePayResponseModel
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import platform.Foundation.*
import platform.darwin.NSObject

// استخدم object لإنشاء Singleton - سيتم إنشاء نسخة واحدة فقط من هذا الكائن
object BankyLiteNotificationManager {

    private const val NOTIFICATION_NAME = "BankyLiteResponseNotification"
    private const val URL_KEY = "URL_KEY"

    // متغير لتخزين دالة الكولباك التي تأتي من الـ Composable
    private var onPaymentResultCallback: ((BankyLitePayResponseModel) -> Unit)? = null

    // المراقب نفسه. يتم إنشاؤه مرة واحدة فقط
    private val notificationObserver = object : NSObject() {
        @ObjCAction
        fun handleBankyLiteResponse(notification: NSNotification) {
//            myLogger("BankyLitePay [Manager]: Notification Received!")
            val urlString = notification.userInfo?.get(URL_KEY) as? String

//            myLogger("BankyLitePay [urlString]: $urlString")
            if (urlString != null) {
                val map = parseUrlToMap(urlString)
//                val parseUrl = parseUrlQueryToMultiMap(urlString)
//                myLogger("BankyLitePay [parseUrl]: $parseUrl")
                // ... نفس منطق تحليل الرابط الخاص بك ...
//                val receivedUrl = NSURL(string = urlString)
//                myLogger("BankyLitePay [receivedUrl]: $receivedUrl")
//                val components = NSURLComponents.componentsWithURL(receivedUrl, resolvingAgainstBaseURL = false)
//                val components = NSURLComponents.componentsWithString(urlString)
//                myLogger("BankyLitePay [components receivedUrl]: $components")
//                myLogger("BankyLitePay [components receivedUrl queryItems]: ${components?.queryItems}")
//                myLogger("BankyLitePay [map] : $map")
//                myLogger("BankyLitePay [map[\"externalReferenceId\"]] : ${map["externalReferenceId"]}")

                var status : Boolean?
                var result : String?
                var error: String?
                var code : String?

                if (map.containsKey("status")) {
                    status = true
                    result = map["externalReferenceId"]
                    error = null
                    code = null
                }else{
                    if (map.containsKey("message")) {
                        error = map["message"]
                    }else{
                        error = "Payment Failed"
                    }
                    status = false
                    result = "Payment Failed"
                    code = "-1"
                }

//                var status = false
//                var result = "Payment Failed"
//                var error: String? = null
//                var code = "-1"

//                components?.queryItems?.forEach { item ->
//                    val queryItem = item as NSURLQueryItem
//                    myLogger("BankyLitePay [queryItem]: $queryItem")
//                    /// queryItem.name -> status, result, error, code
////                    queryItem.name?.let { myLogger("BankyLitePay [queryItem.name]: $it") }
//                    when (queryItem.name) {
//                        "status" -> status = queryItem.value?.toBooleanStrictOrNull() ?: false
//                        "result" -> result = queryItem.value ?: "Payment Failed"
//                        "error" -> error = queryItem.value
//                        "code" -> code = queryItem.value ?: "-1"
//                    }
//                }
                // استدعاء الكولباك وتمرير النتيجة إلى الـ Composable
                onPaymentResultCallback?.invoke(BankyLitePayResponseModel(status, result, error, code))
//                myLogger("BankyLitePay [onPaymentResultCallback]: $onPaymentResultCallback")
            } else {
                onPaymentResultCallback?.invoke(BankyLitePayResponseModel(false, "Failed to receive response", "No URL received", "-1"))
            }
        }
    }

    // دالة لتسجيل المراقب في NotificationCenter
    fun register() {
//        myLogger("BankyLitePay [Manager]: Registering observer...")
        NSNotificationCenter.defaultCenter.addObserver(
            observer = notificationObserver,
            selector = NSSelectorFromString("handleBankyLiteResponse:"),
            name = NOTIFICATION_NAME,
            `object` = null
        )
    }

    // دالة لبدء الاستماع وتحديد الكولباك
    fun startObserving(callback: (BankyLitePayResponseModel) -> Unit) {
//        myLogger("BankyLitePay [Manager]: Start Observing.")
        this.onPaymentResultCallback = callback
    }

    // دالة لإيقاف الاستماع وتنظيف الكولباك
    fun stopObserving() {
//        myLogger("BankyLitePay [Manager]: Stop Observing.")
        this.onPaymentResultCallback = null
    }


    fun parseUrlToMap(url: String): Map<String, String> {
        val query = url.substringAfter("?", "")
        if (query.isBlank()) return emptyMap()

        return query.split("&").mapNotNull { param ->
            val parts = param.split("=", limit = 2)
            if (parts.size == 2) {
                val key = decodeUrl(parts[0])
                val value = decodeUrl(parts[1])
                key to value
            } else null
        }.toMap()
    }

    fun decodeUrl(value: String): String {
        return value
            .replace("+", " ")
            .replace(Regex("%([0-9A-Fa-f]{2})")) {
                it.groupValues[1].toInt(16).toChar().toString()
            }
    }


    fun parseStatusJson(status: String) {
        val json = Json.parseToJsonElement(status).jsonObject
        println(json["result"]) // SUCCESSFUL
    }

}