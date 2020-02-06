package dev.forcetower.cubicrectangle.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.AppExecutors
import dev.forcetower.cubicrectangle.CoroutineTestRule
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.core.services.FakeTMDbService
import dev.forcetower.cubicrectangle.extensions.mock
import dev.forcetower.cubicrectangle.model.database.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SearchPresenterTest {
    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var view: SearchContract.View

    @Captor
    private lateinit var captor: ArgumentCaptor<PagedList<Movie>>

    private val observer: Observer<PagedList<Movie>> = mock()

    private val service = FakeTMDbService()
    private val repository = MoviesRepository(service, AppExecutors())

    private lateinit var presenter: SearchPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = SearchPresenter(repository)
        presenter.attach(view)
        presenter.listing.observeForever(observer)
    }

    @Test
    fun emptyQueryYieldsEmptySource() {
        presenter.search("avengers", MainScope())
        Thread.sleep(100)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertTrue(value.isNotEmpty())
        }

        presenter.search("", MainScope())
        Thread.sleep(100)
        captor.run {
            verify(observer, times(2)).onChanged(capture())
            assertTrue(value.isEmpty())
        }
    }

    @Test
    fun changeQueryAlsoChangesSource() {
        presenter.search("avengers", MainScope())
        Thread.sleep(100)
        val first = captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertTrue(value.isNotEmpty())
            value
        }

        presenter.search("avengers 2", MainScope())
        Thread.sleep(100)
        captor.run {
            verify(observer, times(2)).onChanged(capture())
            assertTrue(value.isNotEmpty())
            assertNotSame(first, value)
        }
    }

    @Test
    fun fetchErrorInvokesViewOnError() {
        service.failsWith = "forced error"

        presenter.search("error", MainScope())
        Thread.sleep(100)
        val intCaptor = ArgumentCaptor.forClass(Int::class.java)
        intCaptor.run {
            verify(view, times(1)).onLoadError(capture())
            // view never went to listing state
            verify(view, times(0)).moveToListingState()
            assertEquals(value, R.string.network_error)
        }

        presenter.search("cant load...", MainScope())
        Thread.sleep(100)
        intCaptor.run {
            verify(view, times(2)).moveToErrorState()
            verify(view, times(2)).onLoadError(capture())
            // view never went to listing state
            verify(view, times(0)).moveToListingState()
            assertEquals(value, R.string.network_error)
        }

        captor.run {
            verify(observer, times(2)).onChanged(capture())
            assertTrue(value.isEmpty())
        }

        service.failsWith = null
        presenter.search("not error", MainScope())
        Thread.sleep(100)
        captor.run {
            verify(observer, times(3)).onChanged(capture())
            // view can finally go to listing state
            verify(view, times(1)).moveToListingState()
            assertTrue(value.isNotEmpty())
        }
    }

    @Test
    fun callRefreshUpdatesList() {
        presenter.search("avengers", MainScope())
        Thread.sleep(100)
        val first = captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertTrue(value.isNotEmpty())
            value
        }

        presenter.refresh()
        Thread.sleep(100)
        captor.run {
            verify(observer, times(2)).onChanged(capture())
            assertTrue(value.isNotEmpty())
            assertNotEquals(first, value)
        }
    }
}