package soko.ekibun.bangumi.plugin.logcat.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

object AppUtil {
    /**
     * 分享图片
     * @param context Context
     * @param drawable Drawable
     */
    fun shareFile(context: Context, imageFile: File) {
        try {
            val contentUri = FileProvider.getUriForFile(context, "soko.ekibun.bangumi.fileprovider", imageFile)

            if (contentUri != null) {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                shareIntent.setDataAndType(contentUri, "text/plain")
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                context.startActivity(Intent.createChooser(shareIntent, "发送日志"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
