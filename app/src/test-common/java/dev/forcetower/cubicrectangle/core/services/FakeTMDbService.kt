package dev.forcetower.cubicrectangle.core.services

import dev.forcetower.cubicrectangle.model.database.Genre
import dev.forcetower.cubicrectangle.model.dto.MovieDetailed
import dev.forcetower.cubicrectangle.model.dto.responses.GenresResponse
import dev.forcetower.cubicrectangle.model.dto.responses.MoviesResponse
import java.io.IOException

class FakeTMDbService : TMDbService {
    var failsWith: String? = null
    override suspend fun genres(): GenresResponse {
        return GenresResponse(
            listOf(
                Genre(1L, "A"),
                Genre(2L, "B"),
                Genre(3L, "C")
            )
        )
    }

    override suspend fun moviesPopular(page: Int): MoviesResponse {
        if (failsWith != null) {
            throw IOException(failsWith)
        }

        return MoviesResponse(
            page,
            20,
            1,
            (0..20).map { MovieFactory.createMovieSimple("popular") }
        )
    }

    override suspend fun movieDetails(movieId: Long, append: String): MovieDetailed {
        if (failsWith != null) {
            throw IOException(failsWith)
        }
        return MovieFactory.createMovieDetailed()
    }

    override suspend fun searchMovie(query: String, page: Int): MoviesResponse {
        if (failsWith != null) {
            throw IOException(failsWith)
        }

        return MoviesResponse(
            page,
            20,
            1,
            (0..20).map { MovieFactory.createMovieSimple(query) }
        )
    }

    override suspend fun moviesByGenre(genre: String, page: Int): MoviesResponse {
        if (failsWith != null) {
            throw IOException(failsWith)
        }
        return MoviesResponse(
            page,
            20,
            1,
            (0..20).map { MovieFactory.createMovieSimple(genres = genre) }
        )
    }
}
