package com.akashdev.kotlinx

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

val TextView.value get() = text.toString().trim()

fun TextView.setDrawable(
    start: Int? = 0,
    top: Int? = 0,
    end: Int? = 0,
    bottom: Int? = 0
) = setCompoundDrawablesWithIntrinsicBounds(start!!, top!!, end!!, bottom!!)

fun TextView.setDrawable(
    start: Drawable? = null,
    top: Drawable? = null,
    end: Drawable? = null,
    bottom: Drawable? = null
) = setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)


fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}
