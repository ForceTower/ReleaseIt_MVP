package dev.forcetower.cubicrectangle.core.services

import dev.forcetower.cubicrectangle.model.database.toMovie
import dev.forcetower.cubicrectangle.model.dto.MovieDetailed
import dev.forcetower.cubicrectangle.model.dto.responses.GenresResponse
import dev.forcetower.cubicrectangle.model.dto.responses.MoviesResponse
import java.io.IOException

class FakeTMDbService : TMDbService {
    var failsWith: String? = null
    override suspend fun genres(): GenresResponse {
        TODO("unsupported")
    }

    override suspend fun moviesPopular(page: Int): MoviesResponse {
        if (failsWith != null) {
            throw IOException(failsWith)
        }

        return MoviesResponse(
            page,
            10,
            1,
            (0..10).map { MovieFactory.createMovieSimple("popular") }
        )
    }

    override suspend fun movieDetails(movieId: Long, append: String): MovieDetailed {
        if (failsWith != null) {
            throw IOException(failsWith)
        }
        return MovieFactory.createMovieDetailed("a movie $movieId $append")
    }

    override suspend fun searchMovie(query: String, page: Int): MoviesResponse {
        if (failsWith != null) {
            throw IOException(failsWith)
        }

        return MoviesResponse(
            page,
            3,
            1,
            (0..10).map { MovieFactory.createMovieSimple(query) }
        )
    }

    override suspend fun moviesByGenre(genre: String, page: Int): MoviesResponse {
        if (failsWith != null) {
            throw IOException(failsWith)
        }
        return MoviesResponse(
            page,
            10,
            1,
            (0..10).map { MovieFactory.createMovieSimple(genres = genre) }
        )
    }
}