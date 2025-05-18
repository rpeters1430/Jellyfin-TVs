package com.example.jellyfintv.di

import android.content.Context
import com.example.jellyfintv.api.JellyfinApi
import com.example.jellyfintv.api.JellyfinApiImpl
import com.example.jellyfintv.data.preferences.PreferencesManager
import com.sun.tools.javac.util.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { PreferencesManager(androidContext<Context>()) }
    single<JellyfinApi> { JellyfinApiImpl() }
}
