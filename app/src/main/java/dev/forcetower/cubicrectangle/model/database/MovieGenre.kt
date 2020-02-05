package dev.forcetower.cubicrectangle.model.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onDelete = CASCADE),
    ForeignKey(entity = Genre::class, parentColumns = ["id"], childColumns = ["genreId"], onDelete = CASCADE)
], indices = [
    Index(value = ["movieId", "genreId"], unique = true),
    Index(value = ["movieId"]),
    Index(value = ["genreId"])
])
data class MovieGenre(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val movieId: Long,
    val genreId: Long
)