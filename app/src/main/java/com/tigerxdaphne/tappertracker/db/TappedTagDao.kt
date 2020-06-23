package com.tigerxdaphne.tappertracker.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TappedTagDao {

    @Insert
    suspend fun add(tag: TappedTagModel)

    @Update
    suspend fun update(tag: TappedTagModel)

    @Query("SELECT * from tags WHERE id = :id LIMIT 1")
    suspend fun getTag(id: ByteArray): TappedTagModel?

    @Query("SELECT * from tags ORDER BY lastSet DESC")
    fun getAll(): Flow<List<TappedTagModel>>

    @Query("SELECT reminder from tags WHERE reminder >= :after ORDER BY reminder ASC LIMIT 1")
    suspend fun getUpcomingReminder(after: LocalDate): TagReminder?

    @Query("SELECT * from tags WHERE reminder = :date")
    suspend fun getAllRemindersOnDate(date: LocalDate): List<TappedTagModel>
}
