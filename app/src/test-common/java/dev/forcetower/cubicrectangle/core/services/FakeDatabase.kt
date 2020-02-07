package dev.forcetower.cubicrectangle.core.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import dev.forcetower.cubicrectangle.core.persistence.ReleaseDB
import dev.forcetower.cubicrectangle.core.persistence.dao.GenresDao
import dev.forcetower.cubicrectangle.core.persistence.dao.MoviesDao
import dev.forcetower.cubicrectangle.core.ui.lifecycle.EmptyLiveData
import dev.forcetower.cubicrectangle.model.aggregation.MovieAndGenres
import dev.forcetower.cubicrectangle.model.database.Genre
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.model.database.MovieGenre
import dev.forcetower.cubicrectangle.model.database.toMovie
import org.mockito.Mockito.mock

class FakeDatabase : ReleaseDB() {
    override fun movies(): MoviesDao {
        val data = MutableLiveData<MovieAndGenres>()
        return object : MoviesDao() {
            override suspend fun findByIdDirect(id: Long): Movie? = null
            override suspend fun wipeMovieGenres(id: Long) = Unit
            override suspend fun insertGenres(genres: List<MovieGenre>)  = Unit
            override suspend fun insert(movie: Movie) {
                data.postValue(MovieAndGenres(movie, listOf()))
            }
            override suspend fun update(movie: Movie) = Unit
            override fun getMoviesWithGenres(): LiveData<List<MovieAndGenres>> {
                return EmptyLiveData.create()
            }
            override fun getMovieById(id: Long): LiveData<MovieAndGenres> {
                return data.also {
                    it.postValue(MovieAndGenres(
                        MovieFactory.createMovieDetailed().toMovie(),
                        listOf()
                    ))
                }
            }
            override fun getIds(map: List<Long>): LiveData<List<MovieAndGenres>> {
                return EmptyLiveData.create()
            }

            override fun insertTesting(movie: Movie) = Unit
        }
    }

    override fun genres(): GenresDao {
        return object : GenresDao {
            override suspend fun insert(genres: List<Genre>) = Unit
            override suspend fun empty() = false
            override fun insertTesting(genres: List<Genre>) {}
        }
    }

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        return mock(SupportSQLiteOpenHelper::class.java)
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return mock(InvalidationTracker::class.java)
    }

    override fun clearAllTables() {

    }
}