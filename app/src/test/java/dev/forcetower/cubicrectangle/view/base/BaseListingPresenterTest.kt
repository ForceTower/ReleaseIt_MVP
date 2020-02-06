package dev.forcetower.cubicrectangle.view.base

import androidx.lifecycle.Observer
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.extensions.mock
import dev.forcetower.cubicrectangle.model.database.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.mockito.ArgumentCaptor
import org.mockito.Captor


@ExperimentalCoroutinesApi
open class BaseListingPresenterTest : BaseTest() {
    @Captor
    internal lateinit var captor: ArgumentCaptor<PagedList<Movie>>
    internal val observer: Observer<PagedList<Movie>> = mock()
}