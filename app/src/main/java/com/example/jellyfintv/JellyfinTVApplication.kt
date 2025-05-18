package com.example.jellyfintv

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.jellyfintv.di.appModule
import com.example.jellyfintv.di.networkModule
import com.example.jellyfintv.di.repositoryModule
import com.example.jellyfintv.di.viewModelModule
import com.example.jellyfintv.util.Prefs
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 * The main application class for JellyfinTV.
 */
class JellyfinTVApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize Koin for dependency injection
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@JellyfinTVApplication)
            modules(listOf(appModule, networkModule, repositoryModule, viewModelModule))
        }

        // Initialize Timber for logging in debug builds
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Apply theme based on user preference
        applyTheme()

        // Initialize Prefs
        Prefs.getInstance()
    }

    /**
     * Applies the theme based on user preference.
     * Can be one of: "Light", "Dark", or "System".
     */
    private fun applyTheme() {
        when (Prefs.getInstance().theme) {
            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    companion object {
        @get:Synchronized
        lateinit var instance: JellyfinTVApplication
            private set

        /**
         * Provides application context.
         * This is useful for getting context in classes that don't have a context.
         * Use this sparingly and prefer dependency injection when possible.
         */
        val appContext: Context
            get() = instance.applicationContext
    }
}
