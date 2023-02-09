package com.akashdev.kotlinx

import android.app.KeyguardManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

fun Context.isAutoTime(): Boolean {
    return Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME) == 1
}

fun Context.isPhoneLocked(): Boolean {
    return (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isKeyguardLocked
}

fun Context.isOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return cm.activeNetwork?.let {
        val nc = cm.getNetworkCapabilities(it)
        nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    } ?: false
}

fun Context.defaultKeyboard(): String {
    return Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        .split("/")[0]
}

//Toast
fun Context.toast(data: Any, length: Int? = Toast.LENGTH_LONG) =
    Toast.makeText(this, data.toString(), length!!).show()

fun Context.toast(text: String, length: Int? = Toast.LENGTH_LONG) =
    Toast.makeText(this, text, length!!).show()

fun Context.toast(resId: Int, length: Int? = Toast.LENGTH_LONG) = toast(getString(resId), length!!)

//Open URL
fun Context.openURL(url: String, flags: Int? = null) {
    runCatching {
        Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            flags?.let { this.flags = it }
            startActivity(this)
        }
    }.onFailure { toast("${it.message}") }
}

//Copy text to clipboard
fun Context.copyToClipboard(text: String, label: String = "") {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))
    toast("$label Copy successfully".trim())
}

// Share Text
fun Context.shareText(message: String, appId: String? = null) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, message.replace("\\n", "\n"))
    intent.putExtra(Intent.EXTRA_TITLE, message.replace("\\n", "\n"))
    appId?.let { intent.setPackage(it) }

    when {
        appId.isNullOrBlank() -> startActivity(intent)
        isPackageInstalled(appId) -> startActivity(intent)
        else -> toast("App not installed")
    }
}

// Send Email
fun Context.email(emails: Array<String>, subject: String, message: String? = null) {
    Intent(Intent.ACTION_SENDTO, "mailto:".toUri()).run {
        putExtra(Intent.EXTRA_EMAIL, emails)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, message)
        if (resolveActivity(packageManager) != null) {
            startActivity(this)
        }
    }
}

fun View.hideKeyboard() {
    val inputMethodManager: InputMethodManager? =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.inflate(
    layoutId: Int,
    viewGroup: ViewGroup? = null,
    attachToRoot: Boolean = false,
): View = LayoutInflater.from(this).inflate(layoutId, viewGroup, attachToRoot)

fun Context.batteryLevel(): Int {
    return (getSystemService(Context.BATTERY_SERVICE) as BatteryManager).getIntProperty(
        BatteryManager.BATTERY_PROPERTY_CAPACITY
    )
}

fun Context.getNumberOfColumns(@LayoutRes layout: Int): Int {
    val view = View.inflate(this, layout, null)
    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val width = view.measuredWidth
    var count: Int = resources.displayMetrics.widthPixels / width
    val remaining: Int = resources.displayMetrics.widthPixels - width * count
    if (remaining > width - 15) count++
    return count
}