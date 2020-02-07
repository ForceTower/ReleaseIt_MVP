package dev.forcetower.cubicrectangle.view.details

import androidx.lifecycle.Observer
import dev.forcetower.cubicrectangle.extensions.mock
import dev.forcetower.cubicrectangle.model.aggregation.MovieAndGenres
import dev.forcetower.cubicrectangle.view.base.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DetailsPresenterTest : BaseTest() {
    @Mock
    internal val observer: Observer<MovieAndGenres> = mock()
    @Captor
    internal lateinit var captor: ArgumentCaptor<MovieAndGenres>

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
            verify(observer, atLeastOnce()).onChanged(capture())
            assertNotNull(value)
        }
    }

    /**
     * Since the data for details comes from database, the information is always there but without
     * some special fields.
     */
    @Test
    fun neverFails() {
        service.failsWith = "a bad connection"
        presenter.loadMovieDetails(1, MainScope()).observeForever(observer)
        captor.run {
            verify(observer, atLeastOnce()).onChanged(capture())
            assertNotNull(value)
        }
    }
}