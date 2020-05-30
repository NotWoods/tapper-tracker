package com.tigerxdaphne.tappertracker.db

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TappedRepository(
    private val tappedTagDao: TappedTagDao
) {

    suspend fun addTag(tag: TappedTag) {
        tappedTagDao.add(TappedTagModel.fromInterface(tag))
    }

    suspend fun updateTag(tag: TappedTag) {
        tappedTagDao.update(TappedTagModel.fromInterface(tag))
    }

    suspend fun getTag(id: ByteArray): TappedTag? = tappedTagDao.getTag(id)

    fun getAllTags(): Flow<List<TappedTag>> = tappedTagDao.getAll()

    suspend fun getUpcomingReminder(today: LocalDate) =
        tappedTagDao.getUpcomingReminder(today)

    suspend fun getAllRemindersOnDate(date: LocalDate): List<TappedTag> =
        tappedTagDao.getAllRemindersOnDate(date)
}
