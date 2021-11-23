package com.dean.gitmore

import android.app.Application
import com.dean.core.di.databaseModule
import com.dean.core.di.networkModule
import com.dean.core.di.repositoryModule
import com.dean.gitmore.di.useCaseModule
import com.dean.gitmore.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}