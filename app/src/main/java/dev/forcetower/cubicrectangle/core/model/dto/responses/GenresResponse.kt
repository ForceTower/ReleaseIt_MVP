package dev.forcetower.cubicrectangle.core.model.dto.responses

import dev.forcetower.cubicrectangle.core.model.database.Genre

data class GenresResponse(
    val genres: List<Genre>
)