package soko.ekibun.bangumi.plugin.logcat.main

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout

interface IMainActivity {
    val nav_view: View
    val content_frame: FrameLayout
    val mainPresenter: IMainPresenter

    var onBackListener: () -> Boolean

    interface INavigationView {
        val menu: Menu
        fun setCheckedItem(id: Int)
    }

    interface IMainPresenter {
        val drawerView: IDrawerView
    }

    interface IDrawerView {
        var navigationItemSelectedListener: (MenuItem) -> Boolean
        fun select(id: Int)
    }
}
