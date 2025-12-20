package com.mnessim.researchtrackerkmp.domain.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mnessim.Database
import java.util.Properties

actual class DBFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver =
            JdbcSqliteDriver("jdbc:sqlite:terms:db", Properties(), Database.Schema)
        return driver
    }
}