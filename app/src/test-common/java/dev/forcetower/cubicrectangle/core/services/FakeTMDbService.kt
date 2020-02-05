package dev.forcetower.cubicrectangle.core.services

import dev.forcetower.cubicrectangle.model.dto.MovieDetailed
import dev.forcetower.cubicrectangle.model.dto.responses.GenresResponse
import dev.forcetower.cubicrectangle.model.dto.responses.MoviesResponse
import java.io.IOException

class FakeTMDbService : TMDbService {
    override suspend fun genres(): GenresResponse {
        TODO("unsupported")
    }

    override suspend fun moviesPopular(page: Int): MoviesResponse {
        return MoviesResponse(
            page,
            10,
            1,
            (0..10).map { MovieFactory.createMovieSimple("popular") }
        )
    }

    override suspend fun movieDetails(movieId: Long, append: String): MovieDetailed {
        TODO("unsupported")
    }

    override suspend fun searchMovie(query: String, page: Int): MoviesResponse {
        if (query.contains("throw error", ignoreCase = true)) {
            throw IOException("A exception")
        }

        return MoviesResponse(
            page,
            3,
            1,
            (0..10).map { MovieFactory.createMovieSimple(query) }
        )
    }

    override suspend fun moviesByGenre(genre: String, page: Int): MoviesResponse {
        return MoviesResponse(
            page,
            10,
            1,
            (0..10).map { MovieFactory.createMovieSimple(genres = genre) }
        )
    }
}