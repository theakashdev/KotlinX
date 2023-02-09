package com.akashdev.kotlinx

import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment


fun Fragment.isAutoTime(): Boolean {
    return context?.isAutoTime() == true
}


fun Fragment.uninstallApp(packageName: CharSequence) {
    context?.uninstallApp(packageName)
}

fun Fragment.inflate(
    layoutId: Int,
    viewGroup: ViewGroup? = null,
    attachToRoot: Boolean = false,
): View {
    return context!!.inflate(layoutId, viewGroup, attachToRoot)
}


fun Fragment.snackbar(message: String) {
    requireView().snackbar(message = message)
}

fun Fragment.isPhoneLocked(): Boolean {
    return context?.isPhoneLocked() == true
}

fun Fragment.isOnline(): Boolean? {
    return context?.isOnline()
}

fun Fragment.getDrawable(resId: Int): Drawable? {
    return AppCompatResources.getDrawable(requireContext(), resId)
}

fun Fragment.defaultSmsApp(): String? {
    return context?.defaultSmsApp()
}

fun Fragment.defaultCallingApp(): String? {
    return context?.defaultCallingApp()
}

fun Fragment.getAllAppPackages(category: String? = Intent.CATEGORY_LAUNCHER): MutableList<ResolveInfo>? {
    return context?.getAllAppPackages(category)
}

fun Fragment.defaultLauncherApp(): String? {
    return context?.defaultLauncherApp()
}

fun Fragment.getAllKeyboardAppPackages(): List<String>? {
    return context?.getAllKeyboardAppPackages()
}

fun Fragment.defaultKeyboard(): String? {
    return context?.defaultKeyboard()
}

fun Fragment.getAppNameByPackage(pkgName: CharSequence): String? {
    return context?.getAppNameByPackage(pkgName)
}

fun Fragment.getAppIconByPackage(pkgName: CharSequence): Drawable? {
    return context?.getAppIconByPackageName(pkgName)
}

fun Fragment.isBrowserApp(packageName: CharSequence): Boolean {
    return context?.isBrowserApp(packageName) == true
}

fun Fragment.getListOfBrowser(): List<String>? {
    return context?.getInstallBrowsersList()
}

fun Fragment.openURL(url: String, flags: Int? = null) {
    context?.openURL(url, flags)
}

fun Fragment.redirectToBrowser(pkgName: CharSequence, url: String) {
    context?.redirectToBrowser(pkgName, url)
}

fun Fragment.toast(text: CharSequence) {
    requireContext().toast(text)
}

fun Fragment.toast(resId: Int) {
    requireContext().toast(getString(resId))
}

fun Fragment.copyToClipboard(text: String, label: String = "") {
    context?.copyToClipboard(text, label)
}

fun Fragment.shareText(message: String, appId: String? = null) {
    context?.shareText(message, appId)
}

fun Fragment.isPackageInstalled(packageName: CharSequence): Boolean? {
    return context?.isPackageInstalled(packageName)
}

fun Fragment.isSystemApp(packageName: CharSequence): Boolean? {
    return context?.isSystemApp(packageName)
}

fun Fragment.email(emails: Array<String>, subject: String, message: String? = null): Unit? {
    return context?.email(emails, subject, message)
}

fun Fragment.isNewlyInstallApp(packageName: CharSequence): Boolean {
    return context?.isNewlyInstallApp(packageName) == true
}

inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T = this.apply {
    arguments = Bundle().apply(argsBuilder)
}