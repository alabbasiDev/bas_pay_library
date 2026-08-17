package com.superstore.bas_pay

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.superstore.bas_pay.models.DownloadInvoiceModel
import com.superstore.bas_pay.models.DownloadInvoiceResult
import java.io.File

private const val INVOICE_CACHE_DIR = "invoices"
private const val FILE_PROVIDER_SUFFIX = ".baspay.fileprovider"

@Composable
actual fun ShareInvoicePdf(
    request: DownloadInvoiceModel,
    onComplete: (DownloadInvoiceResult) -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(request) {
        onComplete(shareInvoicePdf(context, request))
    }
}

private fun shareInvoicePdf(
    context: Context,
    request: DownloadInvoiceModel,
): DownloadInvoiceResult {
    return try {
        val pdfBytes = request.decodePdfBytes()
        val invoicesDir = File(context.cacheDir, INVOICE_CACHE_DIR)
        if (!invoicesDir.exists() && !invoicesDir.mkdirs()) {
            return DownloadInvoiceResult(
                status = false,
                error = "Unable to create invoice cache directory",
            )
        }
        val pdfFile = File(invoicesDir, request.resolvedFileName())
        pdfFile.writeBytes(pdfBytes)
        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + FILE_PROVIDER_SUFFIX,
            pdfFile,
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = request.mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, request.resolvedFileName()))
        DownloadInvoiceResult(status = true)
    } catch (_: ActivityNotFoundException) {
        DownloadInvoiceResult(
            status = false,
            error = "No application available to share the invoice",
        )
    } catch (error: Exception) {
        DownloadInvoiceResult(
            status = false,
            error = error.message,
        )
    }
}
