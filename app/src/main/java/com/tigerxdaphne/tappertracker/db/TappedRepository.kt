package com.tigerxdaphne.tappertracker.db

class TappedRepository(
    private val tappedTagDao: TappedTagDao
) {
    suspend fun addTag(tag: TappedTag) {
        tappedTagDao.add(tag)
    }

    fun getAllTags() = tappedTagDao.getAll()
}