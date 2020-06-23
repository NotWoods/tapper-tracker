package com.tigerxdaphne.tappertracker.pages.tapped

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneOffset

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ExistingTagTappedViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val existingTag = TappedTagModel(
        id = "123".toByteArray(),
        lastSet = LocalDate.of(2020, 5, 1),
        reminder = LocalDate.of(2020, 5, 3)
    )
    @MockK private lateinit var repository: TappedRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        coEvery { repository.updateTag(any()) } just Runs
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testStop() = testDispatcher.runBlockingTest {
        val args = ExistingTagTappedFragmentArgs(existingTag)
        val date = LocalDate.of(2020, 7, 1)
        val clock = Clock.fixed(date.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC)
        val viewModel = ExistingTagTappedViewModel(args, clock, repository)

        viewModel.stop()

        coVerify {
            repository.updateTag(
                existingTag.copy(lastSet = date, isStopped = true)
            )
        }
    }

    @Test
    fun testReset() = testDispatcher.runBlockingTest {
        val args = ExistingTagTappedFragmentArgs(existingTag)
        val date = LocalDate.of(2020, 7, 1)
        val reminder = LocalDate.of(2020, 7, 3)
        val clock = Clock.fixed(date.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC)
        val viewModel = ExistingTagTappedViewModel(args, clock, repository)

        viewModel.reset()

        coVerify {
            repository.updateTag(
                existingTag.copy(lastSet = date, reminder = reminder, isStopped = false)
            )
        }
    }
}
