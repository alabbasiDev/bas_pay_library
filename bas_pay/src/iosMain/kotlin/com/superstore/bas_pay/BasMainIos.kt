package com.superstore.bas_pay


import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun basMainIosController(trxToken: String, userIdentifier: String?, fullName: String?, language: String?): UIViewController {
    return ComposeUIViewController { // Wrap Composable in a UIViewController
        BasMainIos(trxToken, userIdentifier, fullName, language)
    }
}

@Composable
fun BasMainIos(trxToken: String, userIdentifier: String?, fullName: String?, language: String?) {
//    var basMain:BasMain = BasMain()

    basSdk(trxToken, userIdentifier, fullName, language, "iOS")
}