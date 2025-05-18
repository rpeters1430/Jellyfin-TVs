package com.example.jellyfintv

import android.app.Application
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.jellyfintv.di.appModule
import org.jellyfin.sdk.android.AndroidDevice
import org.jellyfin.sdk.android.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class JellyfinTVApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin for dependency injection
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@JellyfinTVApp)
            modules(appModule)
        }
        
        // Initialize Jellyfin SDK
        try {
            initialize(
                context = this,
                applicationInfo = AndroidDevice.create(
                    applicationContext = this,
                    applicationName = "JellyfinTV",
                    applicationVersion = "1.0.0"
                )
            )
        } catch (e: Exception) {
            Log.e("JellyfinTVApp", "Failed to initialize Jellyfin SDK", e)
        }
        
        // Configure Glide for image loading
        Glide.get(this).apply {
            // Configure Glide options here if needed
        }
    }
    
    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }
    
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }
}
