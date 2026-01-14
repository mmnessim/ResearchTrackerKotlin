package com.mnessim.researchtrackerkmp.domain.repositories

import com.mnessim.Database
import com.mnessim.Terms
import com.mnessim.researchtrackerkmp.domain.models.Term

interface ITermsRepo {
    fun getAllTerms(): List<Term>
    fun getTermById(id: Long): Term?
    fun insertTerm(term: String, locked: Boolean)
    fun updateTerm(term: Term)
    fun deleteTerm(id: Long)
}

open class TermsRepo(private val database: Database) : ITermsRepo {
    private val queries = database.termsDatabaseQueries

    override fun getAllTerms(): List<Term> {
        return queries.selectAll().executeAsList()
            .map { rowToTerm(it) }
    }

    override fun getTermById(id: Long): Term? {
        val row = queries.selectOne(id).executeAsOneOrNull() ?: return null
        return rowToTerm(row)
    }

    override fun insertTerm(term: String, locked: Boolean) {
        queries.insertTerm(term, locked, lastArticleGuid = null, hasNewArticle = false)
    }

    override fun updateTerm(term: Term) {
        queries.updateTerm(
            term = term.term,
            locked = term.locked,
            id = term.id,
            lastArticleGuid = term.lastArticleGuid,
            hasNewArticle = term.hasNewArticle
        )
    }

    override fun deleteTerm(id: Long) {
        queries.deleteTerm(id)
    }

    private fun rowToTerm(row: Terms): Term {
        return Term(
            id = row.id,
            term = row.term,
            locked = row.locked ?: false,
            lastArticleGuid = row.lastArticleGuid,
            hasNewArticle = row.hasNewArticle ?: false
        )
    }
}