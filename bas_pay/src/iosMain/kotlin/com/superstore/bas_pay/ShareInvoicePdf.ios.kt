@file:OptIn(ExperimentalForeignApi::class)

package com.superstore.bas_pay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.superstore.bas_pay.models.DownloadInvoiceModel
import com.superstore.bas_pay.models.DownloadInvoiceResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.popoverPresentationController

@Composable
actual fun ShareInvoicePdf(
    request: DownloadInvoiceModel,
    onComplete: (DownloadInvoiceResult) -> Unit,
) {
    LaunchedEffect(request) {
        onComplete(shareInvoicePdf(request))
    }
}

private fun shareInvoicePdf(request: DownloadInvoiceModel): DownloadInvoiceResult {
    return try {
        val presenter = findTopViewController()
            ?: return DownloadInvoiceResult(
                status = false,
                error = "Unable to present invoice share sheet",
            )
        val pdfBytes = request.decodePdfBytes()
        val filePath = NSTemporaryDirectory() + request.resolvedFileName()
        val fileUrl = NSURL.fileURLWithPath(filePath)
        val pdfData = pdfBytes.toNSData()
        val didWrite = pdfData.writeToURL(fileUrl, atomically = true)
        if (!didWrite) {
            return DownloadInvoiceResult(
                status = false,
                error = "Unable to write invoice file",
            )
        }
        val activityViewController = UIActivityViewController(
            activityItems = listOf(fileUrl),
            applicationActivities = null,
        )
        activityViewController.popoverPresentationController?.sourceView = presenter.view
        presenter.presentViewController(
            activityViewController,
            animated = true,
            completion = null,
        )
        DownloadInvoiceResult(status = true)
    } catch (error: Exception) {
        DownloadInvoiceResult(
            status = false,
            error = error.message,
        )
    }
}

private fun ByteArray.toNSData(): NSData {
    if (isEmpty()) {
        return NSData()
    }
    return usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
    }
}

private fun findTopViewController(): UIViewController? {
    val windows = UIApplication.sharedApplication.windows.mapNotNull { window ->
        window as? UIWindow
    }
    val keyWindow = windows.firstOrNull { window -> window.isKeyWindow() } ?: windows.firstOrNull()
    var controller = keyWindow?.rootViewController
    while (controller?.presentedViewController != null) {
        controller = controller?.presentedViewController
    }
    return controller
}
