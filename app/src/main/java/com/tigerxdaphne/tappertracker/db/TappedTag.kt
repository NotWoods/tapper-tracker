package com.tigerxdaphne.tappertracker.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

/**
 * Stores data about a single NFC tag.
 * @property id - NFC Tag ID, corresponds to [android.nfc.Tag.getId].
 * @property lastSet - Date when the NFC tag was last tapped.
 * @property reminder - When to remind the user about the tag.
 * @property customName Custom name for the tag.
 */
@Parcelize
@Entity(tableName = "tags")
data class TappedTag(
    @PrimaryKey
    val id: ByteArray,
    val lastSet: LocalDate,
    val reminder: LocalDate,
    val customName: String = "",
    val isStopped: Boolean = false
) : Parcelable {

    /**
     * Returns the name. If no custom name was specified,
     * returns the NFC ID as a string.
     */
    val name get() = if (customName.isNotBlank()) customName else String(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TappedTag

        return id.contentEquals(other.id) &&
                reminder == other.reminder &&
                customName == other.customName &&
                lastSet == other.lastSet
    }

    override fun hashCode(): Int {
        var result = id.contentHashCode()
        result = 31 * result + reminder.hashCode()
        result = 31 * result + customName.hashCode()
        result = 31 * result + lastSet.hashCode()
        return result
    }
}
