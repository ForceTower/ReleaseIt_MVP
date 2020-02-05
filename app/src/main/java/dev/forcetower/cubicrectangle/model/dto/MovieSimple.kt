package dev.forcetower.cubicrectangle.model.dto

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate

data class MovieSimple(
    val popularity: Double,
    @SerializedName("vote_count")
    val voteCount: Long,
    val video: Boolean,
    @SerializedName("poster_path")
    val posterPath: String?,
    val id: Long,
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Long>,
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val overview: String?,
    @SerializedName("release_date")
    val releaseDate: LocalDate?
)