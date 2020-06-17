package com.tigerxdaphne.tappertracker.pages

import android.content.Context
import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tigerxdaphne.tappertracker.databinding.ListItemTappedTagBinding
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import com.tigerxdaphne.tappertracker.pages.list.TappedTagViewHolder
import io.mockk.verify
import org.junit.Assert.assertEquals
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
        val context = ApplicationProvider.getApplicationContext<Context>()
        binding = ListItemTappedTagBinding.inflate(LayoutInflater.from(context))
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
            lastUpdatedFormatter
        ).bind(tag, janFirst)

        assertEquals("Battery-powered device", binding.name.text)
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
            lastUpdatedFormatter
        ).bind(tag, janFirst)

        assertEquals("Last tapped on Jan 1, 2020", binding.lastTapped.text)
    }
}
