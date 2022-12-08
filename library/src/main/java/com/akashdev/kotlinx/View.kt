package com.akashdev.kotlinx

import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

// View Visible
fun View.show() {
    if (!isVisible) visibility = View.VISIBLE
}

// View Invisible
fun View.hide() {
    if (isVisible) visibility = View.GONE
}

//Enable View
fun View.enable() {
    isEnabled = true
    alpha = 1f
}

//Disable View
fun View.disable() {
    isEnabled = false
    alpha = 0.7F
}

// View Invisible
fun View.invisible() {
    if (isVisible) visibility = View.INVISIBLE
}

// View Visible
fun View.showWithAnimation(duration: Long? = 300) {
    if (!isVisible) {
        animate().setDuration(duration ?: 0).alpha(1f)
        visibility = View.VISIBLE
    }
}

// View Invisible
fun View.hideWithAnimation(duration: Long? = 300) {
    if (isVisible) {
        animate().setDuration(duration ?: 0).alpha(0f)
        visibility = View.GONE
    }
}

// View Invisible
fun View.invisibleWithAnimation(duration: Long? = 300) {
    if (isVisible) {
        animate().setDuration(duration ?: 0).alpha(0f)
        visibility = View.INVISIBLE
    }
}

fun View.toggleVisibility() = if (isVisible) hide() else show()

fun View.toggleVisibilityWithAnimation(duration: Long? = 150) {
    animate().setDuration(duration ?: 0).alpha(if (isVisible) 0f else 1f)
    if (isVisible) hideWithAnimation() else showWithAnimation()
}

fun View.enable(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    alpha = if (isEnabled) 1F else 0.7F
}

//snackbar
fun View.snackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}

