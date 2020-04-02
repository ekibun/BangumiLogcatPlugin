package soko.ekibun.bangumi.plugin.logcat

import android.app.Activity
import android.content.Context
import androidx.annotation.Keep
import soko.ekibun.bangumi.plugin.logcat.main.MainActivityPlugin
import soko.ekibun.bangumi.plugins.BasePlugin

@Keep
class Plugin : BasePlugin() {
    override val pluginList = mapOf(
        "soko.ekibun.bangumi.ui.main.MainActivity" to MainActivityPlugin()
    )

    @Keep
    override fun setUpPlugins(activity: Activity, context: Context) {
        App.init(activity.application, context)
        super.setUpPlugins(activity, context)
    }
}
