package com.mnessim.researchtrackerkmp.domain.repositories

import com.mnessim.Database

class PreferencesRepo(private val database: Database) {
    private val queries = database.preferencesQueries

    fun getAllPrefs(): Map<String, String> {
        return queries
            .getAllPreferences()
            .executeAsList()
            .associate { it.key to it.value_ }
    }

    fun getPrefByKey(key: String): String? {
        val value = queries.getPreferenceByKey(key).executeAsOneOrNull()?.value_
        return value
    }

    fun insertPref(key: String, value: String) {
        queries.insertPreference(key, value)
    }

    fun updatePref(key: String, value: String) {
        queries.updatePreference(value, key)
    }

    fun deletePref(key: String) {
        queries.deletePreferenceByKey(key)
    }

    fun deleteAll() {
        queries.deleteAllPreferences()
    }
}