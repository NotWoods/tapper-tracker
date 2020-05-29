package com.tigerxdaphne.tappertracker.pages.edit

import android.content.Context
import android.content.DialogInterface
import androidx.navigation.NavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.db.TappedTag
import java.time.LocalDate

/**
 * Builds an alert dialog that should be displayed when exit or back is pressed.
 * Asks the user to confirm they want to exit, or trigger save first.
 */
class ConfirmationAlertDialog(
    context: Context,
    customName: String,
    isNew: Boolean,
    onButtonClick: (DialogInterface, which: Int) -> Unit
) : MaterialAlertDialogBuilder(context) {

    init {
        setTitle(context.getString(R.string.exit_no_save_title))
        setMessage(if (isNew) {
            if (customName.isBlank()) {
                context.getString(R.string.exit_no_save_new_tag_no_name_message)
            } else {
                context.getString(R.string.exit_no_save_new_tag_message, customName)
            }
        } else {
            context.getString(R.string.exit_no_save_existing_tag_message, customName)
        })

        setNegativeButton(context.getString(R.string.cancel), onButtonClick)
        setNegativeButton(context.getString(R.string.exit), onButtonClick)
        setPositiveButton(context.getString(R.string.save), onButtonClick)
    }
}
