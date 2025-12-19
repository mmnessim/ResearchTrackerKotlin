package com.mnessim.researchtrackerkmp.domain.data

import app.cash.sqldelight.db.SqlDriver
import com.mnessim.Terms

// TODO: Create Android and iOS actuals
expect class DBFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DBFactory): Terms {
    val driver = driverFactory.createDriver()
    val database = Terms(driver)
    return database
}