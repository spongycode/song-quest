package com.spongycode.songquest

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import com.spongycode.songquest.Image.saveBitmapAndGetUri
import com.spongycode.songquest.Image.shareImageUriWithText
import com.spongycode.songquest.util.Constants.BASE_URL
import java.io.File
import kotlin.io.outputStream
import kotlin.use

actual fun ImageBitmap.share(
    context: PlatformContext,
    score: Int?,
    category: String?
) {
    val ctx = context.getContext() as Context
    val bitmap = asAndroidBitmap()
    val uri = saveBitmapAndGetUri(ctx, bitmap)
    val message =
        "I scored $score in #$category Quiz! Think you can beat me?\nDownload the app: $BASE_URL"
    shareImageUriWithText(ctx, uri, message)
}

object Image {
    fun saveBitmapAndGetUri(context: Context, bitmap: Bitmap): Uri {
        val filename = "shared_image_${System.currentTimeMillis()}.png"
        val file = File(context.cacheDir, filename)
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun shareImageUriWithText(context: Context, uri: Uri, message: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share your score via"))
    }
}