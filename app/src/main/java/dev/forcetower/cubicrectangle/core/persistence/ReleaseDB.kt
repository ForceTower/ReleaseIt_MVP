package dev.forcetower.cubicrectangle.core.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.cubicrectangle.core.persistence.dao.GenresDao
import dev.forcetower.cubicrectangle.core.persistence.dao.MoviesDao
import dev.forcetower.cubicrectangle.model.database.Genre
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.model.database.MovieGenre

@Database(entities = [
    Movie::class,
    Genre::class,
    MovieGenre::class
], version = 1)
@TypeConverters(value = [Converter::class])
abstract class ReleaseDB : RoomDatabase() {
    abstract fun movies(): MoviesDao
    abstract fun genres(): GenresDao
}