package com.tigerxdaphne.tappertracker.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TappedTagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: TappedTag)

    @Query("SELECT * from tags ORDER BY lastTapped DESC")
    suspend fun getAll(): List<TappedTag>
}