package com.tigerxdaphne.tappertracker.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TappedTagDao {

    @Insert
    suspend fun add(tag: TappedTag)

    @Update
    suspend fun update(tag: TappedTag)

    @Query("SELECT * from tags WHERE id = :id LIMIT 1")
    suspend fun getTag(id: ByteArray): TappedTag?

    @Query("SELECT * from tags ORDER BY lastSet DESC")
    fun getAll(): Flow<List<TappedTag>>

    @Query("SELECT reminder from tags WHERE reminder >= :today ORDER BY reminder ASC LIMIT 1")
    suspend fun getUpcomingReminder(today: LocalDate): TagReminder?

    @Query("SELECT * from tags WHERE reminder = :date")
    suspend fun getAllRemindersOnDate(date: LocalDate): List<TappedTag>
}
