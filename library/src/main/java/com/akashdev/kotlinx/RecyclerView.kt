package com.akashdev.kotlinx

import android.view.View
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.hideViewOnScroll(vararg views: View) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                //Scroll down
                views.forEach { it.hideWithAnimation(500) }
            } else if (dy < 0) {
                //Scroll up
                views.forEach { it.showWithAnimation(500) }
            }
        }
    })
}


fun ScrollView.hideViewOnScroll(vararg views: View) {
    setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
        //Log.d(TAG, "setOnClicks: $scrollY, $oldScrollY")
        if (oldScrollY in 1 until scrollY) {
            //Scroll down
            views.forEach { it.hideWithAnimation(500) }
        } else {
            //Scroll up
            views.forEach { it.showWithAnimation(500) }
        }
    }
}
