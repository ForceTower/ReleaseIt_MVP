package dev.forcetower.cubicrectangle.core.persistence.dao

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.forcetower.cubicrectangle.model.aggregation.MovieAndGenres
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.model.database.MovieGenre
import dev.forcetower.cubicrectangle.model.database.toMovie
import dev.forcetower.cubicrectangle.model.database.updatedWithFields
import dev.forcetower.cubicrectangle.model.dto.MovieDetailed
import dev.forcetower.cubicrectangle.model.dto.MovieSimple

@Dao
abstract class MoviesDao {
    @Query("SELECT * FROM Movie WHERE id = :id")
    protected abstract suspend fun findByIdDirect(id: Long): Movie?

    @Query("DELETE FROM MovieGenre WHERE movieId = :id")
    protected abstract suspend fun wipeMovieGenres(id: Long)

    @Insert
    protected abstract suspend fun insertGenres(genres: List<MovieGenre>)

    @Insert
    abstract suspend fun insert(movie: Movie)

    @Update
    abstract suspend fun update(movie: Movie)

    @Transaction
    open suspend fun insertSimpleList(movies: List<MovieSimple>) {
        movies.forEach {
            val existing = findByIdDirect(it.id)
            if (existing != null) {
                val updated = existing.updatedWithFields(it)
                update(updated)
            } else {
                val movie = it.toMovie()
                insert(movie)
            }

            if (it.genreIds.isNotEmpty()) {
                // take the time to apply a diff?
                wipeMovieGenres(it.id)
                insertGenres(it.genreIds.map { id -> MovieGenre(0, it.id, id) })
            }
        }
    }

    @Transaction
    open suspend fun insertDetailed(detailed: MovieDetailed) {
        val existing = findByIdDirect(detailed.id)
        if (existing != null) {
            val updated = existing.updatedWithFields(detailed)
            update(updated)
        } else {
            val movie = detailed.toMovie()
            insert(movie)
        }

        wipeMovieGenres(detailed.id)
        insertGenres(detailed.genres.map { genre -> MovieGenre(0, detailed.id, genre.id) })
    }

    @Transaction
    @Query("SELECT * FROM Movie ORDER BY popularity DESC")
    abstract fun getMoviesWithGenres(): LiveData<List<MovieAndGenres>>

    @Transaction
    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract fun getMovieById(id: Long): LiveData<MovieAndGenres>

    @Transaction
    @Query("SELECT * FROM Movie WHERE id IN (:map)")
    abstract fun getIds(map: List<Long>): LiveData<List<MovieAndGenres>>

    @VisibleForTesting
    @Insert(onConflict = ABORT)
    abstract fun insertTesting(movie: Movie)
}