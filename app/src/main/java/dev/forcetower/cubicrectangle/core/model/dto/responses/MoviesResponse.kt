package dev.forcetower.cubicrectangle.core.model.dto.responses

import com.google.gson.annotations.SerializedName
import dev.forcetower.cubicrectangle.core.model.dto.MovieSimple

data class MoviesResponse(
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val results: List<MovieSimple>
)