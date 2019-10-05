package io.schrws.cherrysms.android

import android.app.Application
import io.schrws.cherrysms.android.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CherrySMSApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@CherrySMSApplication)
            androidFileProperties()
            modules(listOf(appModule))
        }
    }
}