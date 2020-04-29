package com.tigerxdaphne.tappertracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import org.threeten.bp.Period

/**
 * Stores data about a single NFC tag.
 * @property id - NFC Tag ID, corresponds to [android.nfc.Tag.getId].
 * @property lastTapped - Time and date when the NFC tag was last tapped.
 * @property reminderDuration - Time before reminding the user about the tag.
 * @property customName Custom name for the tag.
 */
@Entity(tableName = "tags")
data class TappedTag(
    @PrimaryKey
    val id: ByteArray,
    val lastTapped: LocalDate,
    val reminderDuration: Period,
    val customName: String = ""
) {

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
                reminderDuration == other.reminderDuration &&
                customName == other.customName &&
                lastTapped == other.lastTapped
    }

    override fun hashCode(): Int {
        var result = id.contentHashCode()
        result = 31 * result + reminderDuration.hashCode()
        result = 31 * result + customName.hashCode()
        result = 31 * result + lastTapped.hashCode()
        return result
    }
}