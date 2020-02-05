package dev.forcetower.cubicrectangle.view.common

import dev.forcetower.cubicrectangle.core.base.BaseContract
import dev.forcetower.cubicrectangle.core.model.database.Movie

interface MovieClickableContract {
    interface View : BaseContract.View {
        fun onMovieClick(movie: Movie)
    }
}