package dev.forcetower.cubicrectangle.dagger.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dev.forcetower.cubicrectangle.BuildConfig
import dev.forcetower.cubicrectangle.Constants
import dev.forcetower.cubicrectangle.CubicApp
import dev.forcetower.cubicrectangle.core.services.converters.TimeConverters
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: CubicApp): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val host = request.url.host
            return if (host.contains(Constants.TMDB_URL, ignoreCase = true)) {
                val headers = request.headers.newBuilder()
                    .add("Accept", "application/json")
                    .build()

                val url = request.url.newBuilder()
                    .addQueryParameter("api_key", Constants.TMDB_API_KEY)
                    .addQueryParameter("language", "pt-BR")
                    .build()

                val renewed = request.newBuilder().url(url).headers(headers).build()

                chain.proceed(renewed)
            } else {
                val nRequest = request.newBuilder().addHeader("Accept", "application/json").build()
                chain.proceed(nRequest)
            }
        }
    }

    @Provides
    @Singleton
    fun provideClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BASIC
                else
                    HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(ZonedDateTime::class.java, TimeConverters.ZDT_DESERIALIZER)
            .registerTypeAdapter(LocalDate::class.java, TimeConverters.LD_DESERIALIZER)
            .serializeNulls()
            .create()
    }
}