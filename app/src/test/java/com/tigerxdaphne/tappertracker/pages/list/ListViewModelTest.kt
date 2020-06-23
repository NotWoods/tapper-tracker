package com.tigerxdaphne.tappertracker.pages.list

import android.content.Context
import android.nfc.NfcAdapter
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTag
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class ListViewModelTest {

    private val clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC)
    @MockK private lateinit var context: Context
    @MockK private lateinit var repository: TappedRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(NfcAdapter::class)

        every { repository.getAllTags() } returns flowOf(emptyList())
    }

    @After
    fun teardown() {
        unmockkStatic(NfcAdapter::class)
    }

    @Test
    fun testTagsLiveData() {
        val tags = listOf<TappedTag>(mockk(), mockk())
        every { repository.getAllTags() } returns flowOf(tags)
        val viewModel = ListViewModel(repository, clock)

        viewModel.tags.observeForever {
            assertEquals(tags, it)
        }
    }

    @Test
    fun testDoesSupportNfc() {
        val viewModel = ListViewModel(repository, clock)

        every { NfcAdapter.getDefaultAdapter(context) } returns mockk()

        assertTrue(viewModel.deviceSupportsNfc(context))
    }

    @Test
    fun testDoesNotSupportNfc() {
        val viewModel = ListViewModel(repository, clock)

        every { NfcAdapter.getDefaultAdapter(context) } returns null

        assertFalse(viewModel.deviceSupportsNfc(context))
    }

    @Test
    fun testDateNotChanged() {
        val viewModel = ListViewModel(repository, clock)

        assertFalse(viewModel.dateChanged())
    }
}
