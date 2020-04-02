package soko.ekibun.bangumi.plugin.logcat

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import soko.ekibun.bangumi.plugin.logcat.util.AppUtil
import java.io.File
import java.lang.ref.WeakReference


class App(val host: Context, val plugin: Context) {

    val logFile by lazy {
        val cachePath = File(host.cacheDir, "images")
        cachePath.mkdirs() // don't forget to make the directory
        File(cachePath, "logcat")
    }

    var process: Process? = null

    fun startLogcat() {
        Toast.makeText(host, "开始记录程序日志", Toast.LENGTH_SHORT).show()
        Thread {
            Runtime.getRuntime().exec("logcat -c").waitFor()
            if (logFile.exists()) logFile.delete()
            process = Runtime.getRuntime().exec("logcat -f ${logFile.path} -v color")
        }.start()
    }

    fun stopLogcat(activity: Activity) {
        Thread {
            process?.destroy()
            process?.waitFor()
            process = null
            AppUtil.shareFile(activity, logFile)
        }.start()
    }

    companion object {
        val inited get() = Companion::app.isInitialized

        lateinit var app: App
        fun init(host: Context, plugin: Context) {
            if (!inited) app =
                App(host, plugin)
        }

        fun createThemeContext(activityRef: WeakReference<Activity>): Context {
            val themeContext = object : ContextThemeWrapper(app.plugin, R.style.AppTheme) {
                override fun getApplicationContext(): Context {
                    return this
                }

                override fun getSystemService(name: String): Any? {
                    return when (name) {
                        Context.WINDOW_SERVICE -> activityRef.get()?.getSystemService(name)
                        else -> super.getSystemService(name)
                    }
                }
            }
            activityRef.get()?.let { themeContext.applyOverrideConfiguration(it.resources.configuration) }
            return themeContext
        }
    }
}
