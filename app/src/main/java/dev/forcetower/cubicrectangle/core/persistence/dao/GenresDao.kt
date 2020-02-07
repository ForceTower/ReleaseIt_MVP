package dev.forcetower.cubicrectangle.core.persistence.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.Query
import dev.forcetower.cubicrectangle.model.database.Genre

@Dao
interface GenresDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(genres: List<Genre>)

    @Query("SELECT CASE (SELECT COUNT(*) FROM Genre) WHEN 0 THEN 1 ELSE 0 END AS IsEmpty")
    suspend fun empty(): Boolean

    @VisibleForTesting
    @Insert(onConflict = ABORT)
    fun insertTesting(genres: List<Genre>)
}