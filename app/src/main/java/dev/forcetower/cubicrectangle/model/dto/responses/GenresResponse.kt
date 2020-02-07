package dev.forcetower.cubicrectangle.model.dto.responses

import dev.forcetower.cubicrectangle.model.database.Genre

data class GenresResponse(
    val genres: List<Genre>
)