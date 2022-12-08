package com.akashdev.kotlinx

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.provider.Browser
import android.provider.Telephony
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.net.toUri

fun Context.defaultCallingApp(): String? {
    val intent = Intent(Intent.ACTION_DIAL).addCategory(Intent.CATEGORY_DEFAULT)
    val resolveInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.resolveActivity(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    }
    return resolveInfo?.activityInfo?.packageName
}

fun Context.defaultLauncherApp(): String? {
    val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
    val resolveInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.resolveActivity(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    }
    return resolveInfo?.activityInfo?.packageName
}

fun Context.getAllAppPackages(category: String? = Intent.CATEGORY_LAUNCHER): MutableList<ResolveInfo> {
    val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(category) }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.queryIntentActivities(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.queryIntentActivities(intent, 0)
    }
}

fun Context.getAppNameByPackage(pkgName: CharSequence): String? = runCatching {
    val packageManager = packageManager
    val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(
            /* packageName = */ "$pkgName",
            /* flags = */
            PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.getApplicationInfo("$pkgName", PackageManager.GET_META_DATA)
    }
    packageManager.getApplicationLabel(info).toString()
}.getOrNull()

fun Context.isBrowserApp(packageName: CharSequence): Boolean {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("http://www.google.com")
    intent.setPackage("$packageName")
    val resolveInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.resolveActivity(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    }
    return resolveInfo?.activityInfo?.packageName == packageName
}

fun Context.getInstallBrowsersList(): List<String> = runCatching {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("http://www.google.com")
    val browserList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.queryIntentActivities(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
    }
    browserList.map { it.activityInfo.packageName }
}.getOrDefault(emptyList())


// Check Package Installed
//(Exception will be PackageManager.NameNotFoundException)
fun Context.isPackageInstalled(packageName: CharSequence) = runCatching {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(
            "$packageName",
            PackageManager.PackageInfoFlags.of(PackageManager.MATCH_ALL.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo("$packageName", 0)
    }
    true
}.getOrDefault(false)

fun Context.isSystemApp(packageName: CharSequence) = runCatching {
    val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(
            "$packageName",
            PackageManager.ApplicationInfoFlags.of(PackageManager.MATCH_ALL.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.getApplicationInfo("$packageName", 0)
    }
    info.flags and ApplicationInfo.FLAG_SYSTEM != 0
}.getOrDefault(false)

fun Context.isNewlyInstallApp(packageName: CharSequence): Boolean {
    return runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val firstInstallTime = packageManager.getPackageInfo(
                "$packageName",
                PackageManager.PackageInfoFlags.of(PackageManager.MATCH_ALL.toLong())
            ).firstInstallTime

            val lastUpdateTime = packageManager.getPackageInfo(
                "$packageName",
                PackageManager.PackageInfoFlags.of(PackageManager.MATCH_ALL.toLong())
            ).lastUpdateTime

            firstInstallTime == lastUpdateTime
        } else {
            @Suppress("DEPRECATION")
            val firstInstallTime = packageManager.getPackageInfo("$packageName", 0).firstInstallTime

            @Suppress("DEPRECATION")
            val lastUpdateTime = packageManager.getPackageInfo("$packageName", 0).lastUpdateTime
            firstInstallTime == lastUpdateTime
        }
    }.getOrDefault(false)
}

fun Context.getLauncherActivityName(packageName: CharSequence): String? {
    val launchIntent: Intent? = packageManager.getLaunchIntentForPackage("$packageName")
    return launchIntent?.component?.className
}

fun Context.uninstallApp(packageName: CharSequence) {
    val intent = Intent(Intent.ACTION_DELETE)
    intent.data = "package:$packageName".toUri()
    startActivity(intent)
}

fun Context.defaultSmsApp(): String? = runCatching {
    Telephony.Sms.getDefaultSmsPackage(this)
}.getOrNull()

fun Context.getAllKeyboardAppPackages(): List<String> {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputMethodManager.enabledInputMethodList
        .map { it.packageName }
        .minus("com.google.android.googlequicksearchbox")
}

fun Context.getAppIconByPackageName(packageName: CharSequence) = runCatching {
    packageManager.getApplicationIcon("$packageName")
}.getOrNull()

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

fun Context.getInstallerPackageName(packageName: String): String? {
    return runCatching {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            packageManager.getInstallSourceInfo(packageName).installingPackageName
        } else {
            @Suppress("DEPRECATION")
            packageManager.getInstallerPackageName(packageName)
        }
    }.getOrNull()
}
