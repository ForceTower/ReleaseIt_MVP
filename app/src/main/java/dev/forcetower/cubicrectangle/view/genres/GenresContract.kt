package dev.forcetower.cubicrectangle.view.genres

import dev.forcetower.cubicrectangle.core.base.BaseContract

interface GenresContract {
    interface View : BaseContract.View {
        fun navigateToSearch()
    }

    interface Presenter : BaseContract.Presenter<View>
}