package dev.forcetower.cubicrectangle.core.services

import dev.forcetower.cubicrectangle.model.dto.MovieSimple
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
}