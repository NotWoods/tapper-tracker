package com.tigerxdaphne.tappertracker.pages

import android.app.Activity
import android.view.LayoutInflater
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tigerxdaphne.tappertracker.databinding.ListItemTappedTagBinding
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import com.tigerxdaphne.tappertracker.pages.list.TappedTagViewHolder
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class TappedTagAdapterTest {

    private val janFirst = LocalDate.of(2020, 1, 1)
    private val lastUpdatedFormatter = DateTimeFormatter.ofPattern("MMM d, uuuu", Locale.CANADA)
    private lateinit var binding: ListItemTappedTagBinding

    @Before
    fun setup() {
        val scenario = launchActivity<Activity>()
        scenario.onActivity { activity ->
            binding = ListItemTappedTagBinding.inflate(LayoutInflater.from(activity))
        }
    }

    @Test
    fun bindsCustomName() {
        val tag = TappedTagModel(
            id = "123".toByteArray(),
            lastSet = janFirst,
            reminder = janFirst,
            name = "Battery-powered device"
        )
        TappedTagViewHolder(
            binding,
            janFirst,
            lastUpdatedFormatter
        ).bind(tag)

        verify { binding.name.text = "Battery-powered device" }
    }

    @Test
    fun bindsLastTapped() {
        val tag = TappedTagModel(
            id = "123".toByteArray(),
            lastSet = janFirst,
            reminder = janFirst
        )
        TappedTagViewHolder(
            binding,
            janFirst,
            lastUpdatedFormatter
        ).bind(tag)

        verify { binding.lastTapped.text = "Last tapped on Jan 1, 2020" }
    }
}
