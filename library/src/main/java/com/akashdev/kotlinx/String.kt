package com.akashdev.kotlinx

import android.util.Base64
import android.util.Patterns
import android.webkit.URLUtil
import androidx.core.text.trimmedLength
import java.net.URI
import java.util.*

/*

fun String.findAnyOfWithRegex(regexOrNormalStrings: List<String>): String? {
//    val input = this.lowercase()
    val input = if (this.contains("18+")) {
        this.replace("[/\\-~_]".toRegex(), " ")
    } else {
        this.replace("[+/\\-~_]".toRegex(), " ")
    }.lowercase()

    return regexOrNormalStrings.find { string ->
        val pattern = Pattern.compile(string)
        val matcher = pattern.matcher(input)
        val matchFound = matcher.find()
        if (matchFound) {
            return if (matcher.groupCount() == 0) {
                string.replace(".*", " ").replace("\\", "")
            } else {
                var value = ""
                val list = 1..matcher.groupCount()
                for (i in list) {
                    value += (if (list.last == i) matcher.group(i) else "${matcher.group(i)} ")
                }
                value
            }
        } else false
    }
}
*/

fun String.isValidEmail(): Boolean =
    !isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidURL() = URLUtil.isValidUrl(this) && Patterns.WEB_URL.matcher(this).matches()

fun String.replaceSpecialCharWithStringChar(): String {
    return this.replace("+", "\\+").replace("#", "\\#")
}
/*
fun String.removeSpecialChar(): String {
    val str = this.replace("+", "%2B").replace("%(?![0-9a-fA-F]{2})".toRegex(), "")
    val decoded = URLDecoder.decode(str, "UTF-8")
    val regex = "[\"\\]\\[\\\\\$\\-.,~_;`^':!?<>/|›&@{}()]".toRegex()//skip only -- *#+
    return decoded.lowercase().replace(regex, "").replace("\\s+".toRegex(), " ")
}*/


val String.intValue get() = this.replace("\\D+".toRegex(), "")

fun String.capitalizeFirstLetter(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.removeWhitespaces() = replace(" ", "")

/**
 *
 * Returns Appended three dots(...) at the end of the string if string length > [n]
 * Or Returns string without dots(...) if string length <= [n]
 * */
fun String.takeWithDotted(n: Int): String {
    return if (trimmedLength() > n) "${take(n)}..." else this
}


fun CharSequence.lowercase(): String = this.toString().lowercase(Locale.ROOT)


fun String.getAmPmFromTime(): String = when {
    contains("AM", true) -> "AM"
    contains("PM", true) -> "PM"
    else -> ""
}


fun String.contains(contains: String, notContains: String, ignoreCase: Boolean? = true): Boolean {
    return if (ignoreCase == true) {
        this.contains("(?=.*$contains)(?!.*$notContains)(.+)".toRegex(RegexOption.IGNORE_CASE))
    } else {
        this.contains("(?=.*$contains)(?!.*$notContains)(.+)".toRegex())
    }
}

fun String?.stringToURL(): URI? {
    return runCatching {
        val urlString = if (this?.startsWith("http") == true) this else "https://$this"
        URI.create(urlString)
    }.getOrNull()
}


fun String.removeLastSlash(): String = if (endsWith("/")) substring(0, lastIndexOf("/")) else this

fun String.timeWithoutAmPm(): String = when {
    //time is 12 hours
    length > 5 -> replaceAfter(" ", "").removeWhitespaces()

    //time is 24 hours
    else -> this
}

fun CharSequence?.toIntOrZero() = this.toString().toIntOrNull() ?: 0

fun CharSequence?.wordCount() = this?.split("\\s+".toRegex())?.size ?: 0


fun String.encodeToBase64(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.DEFAULT)
}

fun String.decodeToBase64(): String {
    return Base64.decode(this, Base64.DEFAULT).toString(charset("UTF-8"))
}
