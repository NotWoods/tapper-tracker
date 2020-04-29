package com.tigerxdaphne.tappertracker.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TappedTagDao {

    @Insert
    suspend fun add(tag: TappedTag)

    @Update
    suspend fun update(tag: TappedTag)

    @Query("SELECT * from tags ORDER BY lastTapped DESC")
    fun getAll(): Flow<List<TappedTag>>
}