package com.mnessim.researchtrackerkmp.domain.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mnessim.Terms

actual class DBFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Terms.Schema, context, "terms.db")
    }
}