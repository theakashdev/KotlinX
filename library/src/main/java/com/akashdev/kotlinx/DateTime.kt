package com.akashdev.kotlinx

import android.content.Context
import android.text.format.DateFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


fun Double.roundOffDecimal(): Double {
    return BigDecimal(this).setScale(1, RoundingMode.FLOOR).toDouble()
}

fun Long.millisToDaysDouble(): Double {
    // google said divide millis with 8.64e+7 to get days in flout
    val days = this / 8.64e+7//it wil return something like 1.01391313112212
    return days.roundOffDecimal()
}

fun convertMinutesToHrMin(timeInMinutes: Int, context: Context): String {
    val time = timeInMinutes.minToMillis().toHrMin()
    if (DateFormat.is24HourFormat(context)) return time
    return runCatching {
        val sdf = SimpleDateFormat("H:mm", Locale.getDefault())
        val dateObj: Date = sdf.parse(time)
        SimpleDateFormat("hh:mma", Locale.getDefault()).format(dateObj)
    }.getOrDefault(time)
}

fun currentClockMinutes(): Int {
    val calendar = Calendar.getInstance(Locale.getDefault())
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)
    return hour * 60 + minutes
}

fun todayDate(
    format: String? = "dd/MMM/yyyy",
    locale: Locale? = Locale.getDefault(),
): String {
    val calendar = Calendar.getInstance()
    val date: Date = calendar.time
    val formatter: Format = SimpleDateFormat(format, locale)
    return formatter.format(date)
}

fun tomorrowDate(
    format: String? = "dd/MMM/yyyy",
    locale: Locale? = Locale.getDefault(),
): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, 1)
    val date: Date = calendar.time
    val formatter: Format = SimpleDateFormat(format, locale)
    return formatter.format(date)
}


fun currentMillis() = System.currentTimeMillis()

fun Int.minToMillis() = TimeUnit.MINUTES.toMillis(this.toLong())
fun Int.hrToMillis() = TimeUnit.HOURS.toMillis(this.toLong())
fun Int.secToMillis() = TimeUnit.SECONDS.toMillis(this.toLong())
fun Int.daysToMillis() = TimeUnit.DAYS.toMillis(this.toLong())

fun Long.millisToSec() = TimeUnit.MILLISECONDS.toSeconds(this)
fun Long.millisToMin() = TimeUnit.MILLISECONDS.toMinutes(this)
fun Long.millisToHr() = TimeUnit.MILLISECONDS.toHours(this)
fun Long.millisToDays() = (this / 8.64e+7).roundToInt()//found in google

fun diffInDates(
    startDate: String = "2021-04-20 12:30:50",
    endDate: String = "2020-02-18 14:32:52",
    dateFormat: String = "yyyy-MM-dd hh:mm:ss",
): Long {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    val startTimeMillis = formatter.parse(startDate)!!.time
    val endTimeMillis = formatter.parse(endDate)!!.time
    return endTimeMillis.minus(startTimeMillis)
}

fun currentDateTime(
    format: String = "yyyy-MM-dd hh:mm:ss",
    locale: Locale = Locale.getDefault(),
): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(Calendar.getInstance().time)
}

fun Long.millisToHumanDateTime(
    format: String = "yyyy-MM-dd hh:mm:ss.SSS",
    locale: Locale = Locale.getDefault(),
): String {
    val formatter = SimpleDateFormat(format, locale)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}


fun Long.toDaysHrMinSec() = String.format(
    "%02d:%02d:%02d:%02d",
    TimeUnit.MILLISECONDS.toDays(this) % 24,
    TimeUnit.MILLISECONDS.toHours(this) % 60,
    TimeUnit.MILLISECONDS.toMinutes(this) % 60,
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)

fun Long.toHrMinSec() = String.format(
    "%02d:%02d:%02d",
    TimeUnit.MILLISECONDS.toHours(this) % 60,
    TimeUnit.MILLISECONDS.toMinutes(this) % 60,
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)

fun Long.toHrMin() = String.format(
    "%02d:%02d",
    TimeUnit.MILLISECONDS.toHours(this) % 60,
    TimeUnit.MILLISECONDS.toMinutes(this) % 60,
)

fun Long.toMinSec() = String.format(
    "%02d:%02d",
    TimeUnit.MILLISECONDS.toMinutes(this) % 60,
    TimeUnit.MILLISECONDS.toSeconds(this) % 60
)

