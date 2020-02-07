package dev.forcetower.cubicrectangle.view.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.forcetower.cubicrectangle.AppExecutors
import dev.forcetower.cubicrectangle.CoroutineTestRule
import dev.forcetower.cubicrectangle.core.persistence.ReleaseDB
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.core.services.FakeDatabase
import dev.forcetower.cubicrectangle.core.services.FakeTMDbService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
open class BaseTest {
    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val database: ReleaseDB = FakeDatabase()

    internal val service = FakeTMDbService()
    internal val repository: MoviesRepository
        get() = MoviesRepository(database, service, AppExecutors())
}