package com.tigerxdaphne.tappertracker.pages.tapped

import android.content.Context
import android.nfc.Tag
import androidx.navigation.NavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tigerxdaphne.tappertracker.NavGraphDirections
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.db.TappedTag
import com.tigerxdaphne.tappertracker.pages.edit.EditFragmentArgs
import java.lang.IllegalStateException
import java.time.LocalDate

/**
 * Builds an alert dialog to display when a new tag is tapped.
 * Call [show] to display the alert.
 */
class NewTagTappedAlertDialog(context: Context) : MaterialAlertDialogBuilder(context) {

    init {
        setTitle(R.string.new_tag_title)
        setMessage(R.string.new_tag_message)

        setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }
        setPositiveButton(R.string.save) { _, _ ->
            throw IllegalStateException("This callback should be overridden before showing the dialog")
        }
    }
}
