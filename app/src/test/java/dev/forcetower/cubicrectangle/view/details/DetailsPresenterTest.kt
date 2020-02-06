package dev.forcetower.cubicrectangle.view.details

import androidx.lifecycle.Observer
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.extensions.mock
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.view.base.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DetailsPresenterTest : BaseTest() {
    @Mock
    internal val observer: Observer<Movie> = mock()
    @Captor
    internal lateinit var captor: ArgumentCaptor<Movie>
    @Captor
    internal lateinit var intCaptor: ArgumentCaptor<Int>

    @Mock
    private lateinit var view: DetailsContract.View
    private lateinit var presenter: DetailsContract.Presenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        service.failsWith = null
        presenter = DetailsPresenter(repository)
        presenter.attach(view)
    }

    @Test
    fun loadSuccess() {
        presenter.loadMovieDetails(1, MainScope()).observeForever(observer)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertNotNull(value)
        }
    }

    @Test
    fun loadFailed() {
        service.failsWith = "a critical error"
        presenter.loadMovieDetails(1, MainScope()).observeForever(observer)
        intCaptor.run {
            verify(view, times(1)).onLoadError(capture())
            assertEquals(R.string.network_error, value)
        }
    }
}