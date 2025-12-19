package com.mnessim.researchtrackerkmp.domain.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.mnessim.Terms

actual class DBFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Terms.Schema, "terms.db")
    }
}