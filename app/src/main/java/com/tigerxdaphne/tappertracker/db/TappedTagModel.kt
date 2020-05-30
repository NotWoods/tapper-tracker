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
 * @property name - Custom name for the tag.
 */
@Parcelize
@Entity(tableName = "tags")
data class TappedTagModel(
    @PrimaryKey
    override val id: ByteArray,
    override val lastSet: LocalDate,
    override val reminder: LocalDate,
    override val name: String = "",
    override val notes: String = "",
    override val isStopped: Boolean = false
) : TappedTag, Parcelable {

    companion object {
        fun fromToday(id: ByteArray, today: LocalDate) = TappedTagModel(
            id = id,
            lastSet = today,
            reminder = today.plusWeeks(1)
        )

        fun fromInterface(other: TappedTag): TappedTagModel {
            if (other is TappedTagModel) return other
            return TappedTagModel(
                id = other.id,
                lastSet = other.lastSet,
                reminder = other.reminder,
                name = other.name,
                notes = other.notes,
                isStopped = other.isStopped
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TappedTag) return false

        return equalTags(this, other)
    }

    override fun hashCode(): Int {
        var result = id.contentHashCode()
        result = 31 * result + lastSet.hashCode()
        result = 31 * result + reminder.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + isStopped.hashCode()
        return result
    }
}
