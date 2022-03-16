package com.akashdev.kotlinx

import android.app.KeyguardManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.BatteryManager
import android.provider.Browser
import android.provider.Settings
import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

fun Context.isAutoTime(): Boolean {
    return Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME) == 1
}


fun Context.uninstallApp(packageName: CharSequence) {
    val intent = Intent(Intent.ACTION_DELETE)
    intent.data = "package:$packageName".toUri()
    startActivity(intent)
}
/*

fun Context.openActivity(
    activity: Class<*>,
    flags: Int? = null,
    bundle: Bundle? = null
) {
    Intent(this, activity).apply {
        flags?.let { this.flags = it }
        try {
            startActivity(this, bundle)
        } catch (e: IllegalArgumentException) {
            startActivity(this)
        }
    }
}

fun Context.openActivity(action: String, flags: Int? = null, bundle: Bundle? = null) {
    Intent(action).apply {
        flags?.let { this.flags = it }
        try {
            startActivity(this, bundle)
        } catch (e: IllegalArgumentException) {
            startActivity(this)
        }
    }
}
*/

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

fun Context.defaultSmsApp(): String? = runCatching {
    Telephony.Sms.getDefaultSmsPackage(this)
}.getOrNull()

fun Context.defaultCallingApp(): String? {
    val intent = Intent(Intent.ACTION_DIAL).addCategory(Intent.CATEGORY_DEFAULT)
    val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return resolveInfo?.activityInfo?.packageName
}

fun Context.getAllAppPackages(category: String? = Intent.CATEGORY_LAUNCHER): MutableList<ResolveInfo> {
    val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(category) }
    return packageManager.queryIntentActivities(intent, 0)
}

fun Context.defaultLauncherApp(): String? {
    val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
    val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return resolveInfo?.activityInfo?.packageName
}

fun Context.getAllKeyboardAppPackages(): List<String> {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputMethodManager.enabledInputMethodList
        .map { it.packageName }
        .minus("com.google.android.googlequicksearchbox")
}

fun Context.defaultKeyboard(): String {
    return Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        .split("/")[0]
}

fun Context.getAppNameByPackage(pkgName: CharSequence): String? = runCatching {
    val packageManager = packageManager
    val info = packageManager.getApplicationInfo("$pkgName", PackageManager.GET_META_DATA)
    packageManager.getApplicationLabel(info).toString()
}.getOrNull()

fun Context.getAppIconByPackageName(packageName: CharSequence) = runCatching {
    packageManager.getApplicationIcon("$packageName")
}.getOrNull()

fun Context.isBrowserApp(packageName: CharSequence): Boolean {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("http://www.google.com")
    intent.setPackage("$packageName")
    val resolveInfo = packageManager.resolveActivity(intent, 0)

    return resolveInfo?.activityInfo?.packageName == packageName
}

fun Context.getInstallBrowsersList(): List<String> = runCatching {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("http://www.google.com")
    val browserList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
    browserList.map { it.activityInfo.packageName }
}.getOrDefault(emptyList())

//Open URL
fun Context.openURL(url: String, flags: Int? = null) {
    runCatching {
        Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            flags?.let { this.flags = it }
            startActivity(this)
        }
    }.onFailure { toast("${it.message}") }
}


fun Context.redirectToBrowser(packageName: CharSequence, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        setPackage("$packageName")
        putExtra(Browser.EXTRA_APPLICATION_ID, packageName)
    }
    runCatching {
        startActivity(intent)
    }.onFailure { Log.d(TAG, "redirectBrowser:error ${it.message}") }
}

//Toast
fun Context.toast(data: Any) = Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
fun Context.toast(resId: Int) = toast(getString(resId))


fun Context.copyToClipboard(text: CharSequence, label: String = "") {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))
    toast("$label Copy successfully".trim())
}

// Share
fun Context.share(message: String, appId: String? = null) {
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


// Check Package Installed
fun Context.isPackageInstalled(packageName: CharSequence) = runCatching {
    packageManager.getPackageInfo("$packageName", 0)
    true
}.getOrDefault(false)


fun Context.isSystemApp(packageName: CharSequence) = runCatching {
    val info = packageManager.getApplicationInfo("$packageName", 0)
    info.flags and ApplicationInfo.FLAG_SYSTEM != 0
}.getOrDefault(false)


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

fun Context.isNewlyInstallApp(packageName: CharSequence): Boolean {
    return runCatching {
        val firstInstallTime = packageManager.getPackageInfo("$packageName", 0).firstInstallTime
        val lastUpdateTime = packageManager.getPackageInfo("$packageName", 0).lastUpdateTime
        firstInstallTime == lastUpdateTime
    }.getOrDefault(false)
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


fun Context.getLauncherActivityName(packageName: CharSequence): String? {
    val launchIntent: Intent? = packageManager.getLaunchIntentForPackage("$packageName")
    return launchIntent?.component?.className
}


fun Context.batteryLevel(): Int {
    return (getSystemService(Context.BATTERY_SERVICE) as BatteryManager).getIntProperty(
        BatteryManager.BATTERY_PROPERTY_CAPACITY
    )
}


fun Context.getNumberOfColumns(layout: Int): Int {
    val view = View.inflate(this, layout, null)
    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val width = view.measuredWidth
    var count: Int = resources.displayMetrics.widthPixels / width
    val remaining: Int = resources.displayMetrics.widthPixels - width * count
    if (remaining > width - 15) count++
    return count
}