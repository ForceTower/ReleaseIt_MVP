package dev.forcetower.cubicrectangle.view.search

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    val presenter: SearchContract.Presenter
) : ViewModel()