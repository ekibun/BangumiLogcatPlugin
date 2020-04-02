package soko.ekibun.bangumi.plugin.logcat.main

import android.app.Activity
import android.view.LayoutInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.switch_item.view.*
import soko.ekibun.bangumi.plugin.logcat.App
import soko.ekibun.bangumi.plugin.logcat.R
import soko.ekibun.bangumi.plugins.util.ReflectUtil
import soko.ekibun.bangumi.plugins.util.ResourceUtil
import java.lang.ref.WeakReference

class MainPresenter(activityRef: WeakReference<Activity>) {
    val proxy = ReflectUtil.proxyObjectWeak(activityRef, IMainActivity::class.java)!!
    private val nav_view = ReflectUtil.proxyObject(proxy.nav_view, IMainActivity.INavigationView::class.java)!!

    val pluginContext = App.createThemeContext(activityRef)

    val switchView = LayoutInflater.from(App.createThemeContext(activityRef)).inflate(R.layout.switch_item, null)

    init {
        val menu =
            nav_view.menu.add(ResourceUtil.getId(App.app.host, "group2"), 0, 1, "调试日志")
        menu.icon = ResourceUtil.getDrawable(pluginContext, R.drawable.ic_debug)

        menu.actionView = switchView
        menu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        switchView.item_switch.isChecked = App.app.process != null
        switchView.item_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) App.app.startLogcat()
            else activityRef.get()?.let { App.app.stopLogcat(it) }
        }

        val superListener = proxy.mainPresenter.drawerView.navigationItemSelectedListener
        proxy.mainPresenter.drawerView.navigationItemSelectedListener = {
            if (menu == it) {
                switchView.item_switch.isChecked = !switchView.item_switch.isChecked
                false
            } else superListener(it)
        }
    }
}
