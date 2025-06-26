package org.example.project.data

import android.content.Context as Contex
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.expenseApp.db.AppDatabase

actual class DatabaseDriverFactory(private val context: Contex) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "AppDatabase.db"
        )
    }
}