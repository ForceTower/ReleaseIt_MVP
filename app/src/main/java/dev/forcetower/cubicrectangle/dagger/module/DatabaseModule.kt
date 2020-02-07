package dev.forcetower.cubicrectangle.dagger.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.forcetower.cubicrectangle.core.persistence.ReleaseDB
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): ReleaseDB {
        return Room.databaseBuilder(context, ReleaseDB::class.java, "release.it")
            .build()
    }
}