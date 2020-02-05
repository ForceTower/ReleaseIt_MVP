package dev.forcetower.cubicrectangle.model.dto

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

data class ReleaseDates(
    val results: List<ReleaseCountry>
)

data class ReleaseCountry(
    @SerializedName("iso_3166_1")
    val iso: String,
    @SerializedName("release_dates")
    val dates: List<ReleaseDate>
)

data class ReleaseDate(
    val certification: String,
    @SerializedName("iso_639_1")
    val iso: String,
    val note: String?,
    val type: Int,
    @SerializedName("release_date")
    val date: ZonedDateTime
)