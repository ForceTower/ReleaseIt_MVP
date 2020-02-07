package dev.forcetower.cubicrectangle.model.aggregation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import dev.forcetower.cubicrectangle.model.database.Genre
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.model.database.MovieGenre

data class MovieAndGenres(
    @Embedded
    val movie: Movie,
    @Relation(
        entity = Genre::class,
        entityColumn = "id",
        parentColumn = "id",
        associateBy = Junction(value = MovieGenre::class, parentColumn = "movieId", entityColumn = "genreId")
    )
    val genres: List<Genre>
) {
    fun genresString(number: Int = 2) = genres.take(number).joinToString(" / ") { it.name }
}