package com.tigerxdaphne.tappertracker.pages

import android.view.LayoutInflater
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tigerxdaphne.tappertracker.MainActivity
import com.tigerxdaphne.tappertracker.databinding.ListItemTappedTagBinding
import com.tigerxdaphne.tappertracker.db.TappedTag
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class TappedTagAdapterTest {

    private val janFirst = LocalDate.of(2020, 1, 1)
    private val lastUpdatedFormatter = DateTimeFormatter.ofPattern("MMM d, uuuu", Locale.CANADA)
    private lateinit var binding: ListItemTappedTagBinding

    @Before
    fun setup() {
        val scenario = launchActivity<MainActivity>()
        scenario.onActivity { activity ->
            binding = ListItemTappedTagBinding.inflate(LayoutInflater.from(activity))
        }
    }

    @Test
    fun bindsCustomName() {
        val tag = TappedTag(
            id = "123".toByteArray(),
            lastSet = janFirst,
            reminderDuration = Period.ZERO,
            customName = "Battery-powered device"
        )
        TappedTagViewHolder(binding, janFirst, lastUpdatedFormatter).bind(tag)

        verify { binding.name.text = "Battery-powered device" }
    }

    @Test
    fun bindsIdAsFallbackName() {
        val tag = TappedTag(
            id = "123".toByteArray(),
            lastSet = janFirst,
            reminderDuration = Period.ZERO,
            customName = "   "
        )
        TappedTagViewHolder(binding, janFirst, lastUpdatedFormatter).bind(tag)

        verify { binding.name.text = "123" }
    }

    @Test
    fun bindsLastTapped() {
        val tag = TappedTag(
            id = "123".toByteArray(),
            lastSet = janFirst,
            reminderDuration = Period.ZERO
        )
        TappedTagViewHolder(binding, janFirst, lastUpdatedFormatter).bind(tag)

        verify { binding.lastTapped.text = "Last tapped on Jan 1, 2020" }
    }
}