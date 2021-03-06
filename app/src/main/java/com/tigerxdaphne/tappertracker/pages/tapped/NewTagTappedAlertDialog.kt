package com.tigerxdaphne.tappertracker.pages.tapped

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tigerxdaphne.tappertracker.R
import java.lang.IllegalStateException

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
