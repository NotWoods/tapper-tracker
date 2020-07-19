package com.tigerxdaphne.tappertracker.pages.tapped

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ExistingTagTappedFragmentTest {

    private val tag = TappedTagModel(
        id = "123".toByteArray(),
        lastSet = LocalDate.of(2020, 6, 1),
        reminder = LocalDate.of(2020, 7, 1),
        name = "GPS Device"
    )

    @Test
    fun testNameUsedAsTitle() {
        val args = ExistingTagTappedFragmentArgs(tag).toBundle()
        launchFragmentInContainer<ExistingTagTappedFragment>(args)

        onView(withId(R.id.dialogTitle)).check(matches(withText("GPS Device")))
    }
}
