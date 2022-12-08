package com.akashdev.kotlinx

import android.content.res.Resources
import android.util.TypedValue
import com.google.gson.Gson
import kotlin.math.roundToInt
import kotlin.random.Random


/** returns integer dimensional value from the integer px value. */
val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()


val <E> List<E>?.countOrZero: Int
    get() = this?.size ?: 0

fun <A, B> Pair<A, B>?.isNotNull(): Boolean {
    return this != null
}

fun <A, B> Pair<A, B>?.isNull(): Boolean {
    return this == null
}

inline fun <reified T> jsonStringToObject(jsonString: String): T {
    return Gson().fromJson(jsonString, T::class.java)
}

val <T : Any> T.TAG get() = this::class.simpleName

val dialogTag = "Dialog${Random.nextInt()}"
