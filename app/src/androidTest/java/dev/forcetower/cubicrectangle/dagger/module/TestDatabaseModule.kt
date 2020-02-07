package dev.forcetower.cubicrectangle.dagger.module

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dagger.Module
import dagger.Provides
import dev.forcetower.cubicrectangle.core.persistence.ReleaseDB
import javax.inject.Singleton

@Module
object TestDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(): ReleaseDB {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(context, ReleaseDB::class.java)
            .build()
    }
}
