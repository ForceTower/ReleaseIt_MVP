package dev.forcetower.cubicrectangle.core.services.converters

import com.google.gson.JsonDeserializer
import com.google.gson.JsonParseException
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import android.annotation.SuppressLint
import java.util.Locale

private const val formatPattern = "d 'de' MMMM 'de' yyyy"

@SuppressLint("ConstantLocale")
val FORMATTER_MONTH_DAY: DateTimeFormatter =
    DateTimeFormatter.ofPattern(formatPattern, Locale.getDefault())

object TimeConverters {
    @JvmStatic
    fun convertToDayYearFormat(date: LocalDate?): String? = if (date != null) FORMATTER_MONTH_DAY.format(date) else null

    @JvmStatic
    val ZDT_DESERIALIZER: JsonDeserializer<ZonedDateTime> = JsonDeserializer { json, _, _ ->
        val jsonPrimitive = json.asJsonPrimitive
        try {
            // if provided as String - '2011-12-03 10:15:30'
            if (jsonPrimitive.isString) {
                val patterns = arrayOf(
                    "yyyy-MM-dd HH:mm:ss",
                    "yyyy-MM-dd'T'HH:mmX",
                    "yyyy-MM-dd'T'HH:mm:ssX",
                    "yyyy-MM-dd'T'HH:mmZ",
                    "yyyy-MM-dd'T'HH:mm:ssZ",
                    "yyyy-MM-dd'T'HH:mm:ssZ",
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                )
                for (pattern in patterns) {
                    try {
                        val parser = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
                        return@JsonDeserializer ZonedDateTime.parse(jsonPrimitive.asString, parser)
                    } catch (t: Throwable) { }
                }
            }

            // if provided as Long
            if (jsonPrimitive.isNumber) {
                return@JsonDeserializer ZonedDateTime.ofInstant(Instant.ofEpochMilli(jsonPrimitive.asLong), ZoneId.systemDefault())
            }
        } catch (e: RuntimeException) {
            throw JsonParseException("Unable to parse ZonedDateTime", e)
        }

        throw JsonParseException("Unable to parse ZonedDateTime")
    }

    @JvmStatic
    val LD_DESERIALIZER: JsonDeserializer<LocalDate?> = JsonDeserializer { json, _, _ ->
        val jsonPrimitive = json.asJsonPrimitive
        try {
            // if provided as String - '2011-12-03'
            if (jsonPrimitive.isString) {
                val string = jsonPrimitive.asString
                if (string.isBlank()) {
                    return@JsonDeserializer null
                }
                val patterns = arrayOf(
                    "yyyy-MM-dd"
                )
                for (pattern in patterns) {
                    try {
                        val parser = DateTimeFormatter.ofPattern(pattern)
                        return@JsonDeserializer LocalDate.parse(string, parser)
                    } catch (t: Throwable) { }
                }
            }
        } catch (e: RuntimeException) {
            throw JsonParseException("Unable to parse LocalDate", e)
        }

        throw JsonParseException("Unable to parse LocalDate <${jsonPrimitive.asString}>")
    }
}