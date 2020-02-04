package org.omgcobra.matchthese.fragments

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.drawerlayout.widget.DrawerLayout

class DrawerCloseOnBackListener(private val onBackPressedDispatcher: OnBackPressedDispatcher,
                                private val drawerLayout: DrawerLayout): DrawerLayout.DrawerListener {
    var drawerCloseCallback: OnBackPressedCallback? = null

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
    override fun onDrawerStateChanged(newState: Int) {}
    override fun onDrawerOpened(drawerView: View) {
        drawerCloseCallback = onBackPressedDispatcher.addCallback {
            drawerLayout.closeDrawers()
        }
    }
    override fun onDrawerClosed(drawerView: View) {
        drawerCloseCallback?.remove()
        drawerCloseCallback = null
    }
}
