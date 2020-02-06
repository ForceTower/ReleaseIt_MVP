package dev.forcetower.cubicrectangle.core.services

import dev.forcetower.cubicrectangle.model.database.Genre
import dev.forcetower.cubicrectangle.model.dto.MovieDetailed
import dev.forcetower.cubicrectangle.model.dto.MovieSimple
import dev.forcetower.cubicrectangle.model.dto.ReleaseDates
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

object MovieFactory {
    private val counter = AtomicInteger(0)
    fun createMovieSimple(
        name: String = "movie",
        genres: String = "1,2,3"
    ): MovieSimple {
        return MovieSimple(
            Random.nextDouble(),
            Random.nextLong(),
            Random.nextBoolean(),
            null,
            counter.toLong(),
            Random.nextBoolean(),
            null,
            "pt-BR",
            "$name $counter",
            genres.split(",").map { it.toLong() },
            "$name $counter",
            Random.nextDouble(),
            null,
            null
        )
    }

    fun createMovieDetailed(name: String): MovieDetailed {
        return MovieDetailed(
            Random.nextLong(),
            Random.nextBoolean(),
            null,
            null,
            Random.nextLong(),
            listOf(Genre(1L, "Hum..,"), Genre(2L, "Ham...")),
            null,
            null,
            null,
            null,
            null,
            Random.nextDouble(),
            null,
            null,
            null,
            null,
            Random.nextLong(),
            Random.nextInt(),
            null,
            null,
            null,
            name,
            Random.nextBoolean(),
            Random.nextDouble(),
            Random.nextLong(),
            ReleaseDates(emptyList())
        )
    }
}