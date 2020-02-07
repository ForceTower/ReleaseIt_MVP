package dev.forcetower.cubicrectangle.core.persistence

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate

object Converter {
    @TypeConverter
    @JvmStatic
    fun localDateToString(zoned: LocalDate?): String {
        return zoned.toString()
    }

    @TypeConverter
    @JvmStatic
    fun stringToLocalDate(value: String?): LocalDate? {
        return if (value == null || value == "null") null else LocalDate.parse(value)
    }
}