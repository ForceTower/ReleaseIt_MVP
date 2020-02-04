package dev.forcetower.cubicrectangle.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.forcetower.cubicrectangle.core.model.dto.MovieDetailed
import dev.forcetower.cubicrectangle.core.model.dto.MovieSimple
import org.threeten.bp.LocalDate

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val title: String,
    val classification: String?,
    val runtime: Int?,
    val posterPath: String?,
    val backdropPath: String?,
    val voteCount: Int,
    val voteAverage: Double,
    val description: String?,
    val releaseDate: LocalDate?,
    val popularity: Double
)

fun Movie.updatedWithFields(simple: MovieSimple): Movie {
    return this.copy(
        title = simple.title,
        posterPath = simple.posterPath,
        backdropPath = simple.backdropPath,
        voteCount = simple.voteCount,
        voteAverage = simple.voteAverage,
        releaseDate = simple.releaseDate,
        description = simple.overview,
        popularity = simple.popularity
    )
}

fun MovieSimple.toMovie(): Movie {
    return Movie(id, title, null, null, posterPath, backdropPath, voteCount, voteAverage, overview, releaseDate, popularity)
}

fun MovieDetailed.toMovie(): Movie {
    val classification = releaseDates.results
            .firstOrNull { it.iso.equals("br", ignoreCase = true) }
            ?.dates
            ?.firstOrNull()?.certification
    return Movie(id, title, classification, runtime, posterPath, backdropPath, voteCount, voteAverage, overview, releaseDate, popularity)
}

fun Movie.updatedWithFields(detailed: MovieDetailed): Movie {
    val classification = detailed.releaseDates.results
            .firstOrNull { it.iso.equals("br", ignoreCase = true) }
            ?.dates
            ?.firstOrNull()?.certification
    return this.copy(
            title = detailed.title,
            classification = classification,
            runtime = detailed.runtime,
            posterPath = detailed.posterPath,
            backdropPath = detailed.backdropPath,
            voteCount = detailed.voteCount,
            voteAverage = detailed.voteAverage,
            releaseDate = detailed.releaseDate,
            description = detailed.overview,
            popularity = detailed.popularity
    )
}