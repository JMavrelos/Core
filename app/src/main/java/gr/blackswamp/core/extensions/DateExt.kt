@file:Suppress("unused")
package gr.blackswamp.core.extensions

import androidx.annotation.VisibleForTesting
import java.text.SimpleDateFormat
import java.util.*

@Suppress("SpellCheckingInspection")
@VisibleForTesting
internal val formatters = mutableMapOf<String, SimpleDateFormat>()

fun String.asDate(pattern: String): Date {
    val formatter = formatters[pattern] ?: SimpleDateFormat(pattern, Locale.ENGLISH).apply {
        formatters[pattern] = this
    }
    return formatter.parse(this)!! //we want it to crash if there is a problem
}

fun Date.asString(pattern: String): String {
    val formatter = formatters[pattern] ?: SimpleDateFormat(pattern, Locale.ENGLISH).apply {
        formatters[pattern] = this
    }
    return formatter.format(this)
}

private fun Calendar.getFormatted(field: Int, chars: Int = 0): String {
    val reply = this.get(field).toString()
    return "0".repeat((chars - reply.length).coerceAtLeast(0)) + reply
}

fun Calendar.toStartOfDay(): Calendar {
    return this.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

/**
 * used to give the ability to chain adds to Calendar
 */
fun Calendar.withAdded(field: Int, amount: Int): Calendar {
    return this.apply {
        add(field, amount)
    }
}

/**
 * used to give the ability to chain sets to Calendar
 */
fun Calendar.withSet(field: Int, amount: Int): Calendar {
    return this.apply {
        set(field, amount)
    }
}

/**
 * used to give the ability to chain sets to Calendar
 */
fun Calendar.withSet(year: Int, month: Int, day: Int): Calendar {
    return this.apply {
        set(year, month, day)
    }
}

/**
 * used to give the ability to chain time calls to Calendar
 */
fun Calendar.withTime(date: Date): Calendar {
    return this.apply {
        time = date
    }
}

/**
 * used to give the ability to chain time calls to Calendar
 */
fun Calendar.withTime(time: Long): Calendar {
    return this.apply {
        timeInMillis = time
    }
}

fun Calendar.atStartOfDay(): Calendar {
    return this.apply {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}