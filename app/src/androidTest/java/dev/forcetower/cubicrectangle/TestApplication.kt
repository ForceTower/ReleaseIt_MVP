package dev.forcetower.cubicrectangle

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dev.forcetower.cubicrectangle.core.persistence.ReleaseDB
import javax.inject.Inject

class TestApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var component: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var database: ReleaseDB

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

    override fun androidInjector() = component
}