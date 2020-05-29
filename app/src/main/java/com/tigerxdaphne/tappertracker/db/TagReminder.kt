package com.tigerxdaphne.tappertracker.db

import java.time.LocalDate

/**
 * Subset of [TappedTag] that only contains the reminder date.
 * @property reminder - When to remind the user about the tag.
 */
data class TagReminder(val reminder: LocalDate)
