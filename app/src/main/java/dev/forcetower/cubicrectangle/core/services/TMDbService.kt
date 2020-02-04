package dev.forcetower.cubicrectangle.core.services

import dev.forcetower.cubicrectangle.core.model.dto.MovieDetailed
import dev.forcetower.cubicrectangle.core.model.dto.responses.GenresResponse
import dev.forcetower.cubicrectangle.core.model.dto.responses.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbService {
    @GET("genre/movie/list")
    suspend fun genres(): GenresResponse

    @GET("movie/popular?sort_by=releasedate.desc&with_release_type=4")
    suspend fun moviesPopular(@Query("page") page: Int = 1): MoviesResponse

    @GET("movie/{movieId}")
    suspend fun movieDetails(
        @Path("movieId") movieId: Long,
        @Query("append_to_response") append: String = "videos,credits,release_dates"
    ): MovieDetailed

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MoviesResponse

    @GET("discover/movie")
    suspend fun moviesByGenre(
        @Query("with_genre") genre: String,
        @Query("page") page: Int = 1
    ): MoviesResponse
}