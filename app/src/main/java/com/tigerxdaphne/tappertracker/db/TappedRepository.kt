package com.tigerxdaphne.tappertracker.db

import java.time.LocalDate

class TappedRepository(
    private val tappedTagDao: TappedTagDao
) {
    suspend fun addTag(tag: TappedTag) {
        tappedTagDao.add(tag)
    }

    suspend fun updateTag(tag: TappedTag) {
        tappedTagDao.update(tag)
    }

    suspend fun getTag(id: ByteArray) = tappedTagDao.getTag(id)

    fun getAllTags() = tappedTagDao.getAll()

    suspend fun getUpcomingReminder(today: LocalDate) =
        tappedTagDao.getUpcomingReminder(today)

    suspend fun getAllRemindersOnDate(date: LocalDate) = tappedTagDao.getAllRemindersOnDate(date)
}
