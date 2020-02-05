package dev.forcetower.cubicrectangle.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Genre(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String
)