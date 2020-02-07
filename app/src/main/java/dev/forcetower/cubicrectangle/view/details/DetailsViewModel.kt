package dev.forcetower.cubicrectangle.view.details

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    val presenter: DetailsContract.Presenter
) : ViewModel()