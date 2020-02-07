package dev.forcetower.cubicrectangle.dagger.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dev.forcetower.cubicrectangle.Constants
import dev.forcetower.cubicrectangle.core.services.TMDbService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object ServiceModule {
    @Provides
    @Singleton
    fun provideTMDbService(client: OkHttpClient, gson: Gson): TMDbService {
        return Retrofit.Builder()
            .baseUrl(Constants.TMDB_SERVICE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TMDbService::class.java)
    }
}