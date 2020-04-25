package com.tigerxdaphne.tappertracker.db

import javax.inject.Inject

class TappedRepository @Inject constructor(
    private val tappedTagDao: TappedTagDao
) {
}