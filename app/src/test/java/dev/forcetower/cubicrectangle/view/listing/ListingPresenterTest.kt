package dev.forcetower.cubicrectangle.view.listing

import dev.forcetower.cubicrectangle.view.base.BaseListingPresenterTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ListingPresenterTest : BaseListingPresenterTest() {
    @Mock
    private lateinit var view: ListingContract.View
    private lateinit var presenter: ListingPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = ListingPresenter(repository)
        presenter.attach(view)
        presenter.listing.observeForever(observer)
    }

    @Test
    fun fetchSuccessMovesToListingState() {
        presenter.loadMoviesByGenre(2, MainScope())
        Thread.sleep(100)
        verify(view, times(1)).moveToListingState()
        verify(view, times(1)).moveToLoadingState()
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertTrue(value.isNotEmpty())
        }
    }

    @Test
    fun fetchErrorMovesToErrorState() {
        service.failsWith = "forced error"

        presenter.loadMoviesByGenre(2, MainScope())
        Thread.sleep(100)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            verify(view, times(1)).moveToErrorState()
            assertTrue(value.isEmpty())
        }
    }

    @Test
    fun retry() {
        service.failsWith = "forced error"

        presenter.loadMoviesByGenre(1, MainScope())
        Thread.sleep(100)

        verify(view, times(1)).moveToErrorState()
        verify(view, times(0)).moveToListingState()
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertTrue(value.isEmpty())
        }

        service.failsWith = null
        presenter.retry()
        Thread.sleep(100)

        verify(view, times(1)).moveToListingState()
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertTrue(value.isNotEmpty())
        }
    }

    @Test
    fun callRefreshUpdatesList() {
        presenter.loadMoviesByGenre(1, MainScope())
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

    @Test
    fun sameGenreDoesNotUpdateFields() {
        presenter.loadMoviesByGenre(8, MainScope())
        Thread.sleep(100)
        val first = captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertTrue(value.isNotEmpty())
            value
        }

        presenter.loadMoviesByGenre(8, MainScope())
        Thread.sleep(100)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertSame(first, value)
        }
    }

    @Test
    fun changeGenreAlsoChangesSource() {
        presenter.loadMoviesByGenre(8, MainScope())
        Thread.sleep(100)
        val first = captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertTrue(value.isNotEmpty())
            value
        }

        presenter.loadMoviesByGenre(9, MainScope())
        Thread.sleep(100)
        captor.run {
            verify(observer, times(2)).onChanged(capture())
            assertTrue(value.isNotEmpty())
            assertNotSame(first, value)
        }
    }
}