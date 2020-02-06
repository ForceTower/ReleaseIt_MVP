package dev.forcetower.cubicrectangle

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class TestApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var component: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

    override fun androidInjector() = component
}