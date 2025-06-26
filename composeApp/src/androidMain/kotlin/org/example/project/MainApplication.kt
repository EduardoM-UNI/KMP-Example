package org.example.project

import android.app.Application
import com.expenseApp.db.AppDatabase
import org.example.project.data.DatabaseDriverFactory
import org.example.project.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication: Application() {

    override  fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(appModule(AppDatabase.invoke(DatabaseDriverFactory(this@MainApplication).createDriver())))
        }
    }
}