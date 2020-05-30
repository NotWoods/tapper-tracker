package com.tigerxdaphne.tappertracker.db

import java.time.LocalDate

interface TappedTag {
    val id: ByteArray
    val lastSet: LocalDate
    val reminder: LocalDate
    val name: String
    val notes: String
    val isStopped: Boolean
}

fun equalTags(a: TappedTag, b: TappedTag): Boolean {
    if (!a.id.contentEquals(b.id)) return false
    if (a.lastSet != b.lastSet) return false
    if (a.reminder != b.reminder) return false
    if (a.name != b.name) return false
    if (a.notes != b.notes) return false
    if (a.isStopped != b.isStopped) return false

    return true
}
