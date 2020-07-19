package com.tigerxdaphne.tappertracker.pages.edit

import android.content.res.Resources
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import com.tigerxdaphne.tappertracker.notify.AlarmScheduler
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@ExperimentalCoroutinesApi
class EditViewModelTest {

    private val existingTag = TappedTagModel(
        id = "123".toByteArray(),
        lastSet = LocalDate.of(2020, 5, 1),
        reminder = LocalDate.of(2020, 5, 3)
    )
    private val epochClock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC)
    @MockK private lateinit var resources: Resources
    @MockK private lateinit var repository: TappedRepository
    @MockK private lateinit var alarmScheduler: AlarmScheduler

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testDaysUntil() {
        val viewModel = buildViewModel(existingTag)
        assertEquals(10, viewModel.daysUntil(LocalDate.of(2020, 5, 11)))
    }

    private fun buildViewModel(tag: TappedTagModel, isNew: Boolean = true, clock: Clock = epochClock) =
        EditViewModel(
            EditFragmentArgs(tag, isNew),
            resources,
            clock,
            repository,
            alarmScheduler
        )
}
