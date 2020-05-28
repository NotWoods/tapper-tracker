package com.tigerxdaphne.tappertracker.pages.edit

import android.content.Context
import android.content.DialogInterface
import androidx.navigation.NavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.db.TappedTag
import org.threeten.bp.LocalDate

/**
 * Builds an alert dialog to display when a new tag is tapped.
 * Call [show] to display the alert.
 */
class ConfirmationAlertDialog(
    context: Context,
    args: EditFragmentArgs,
    onButtonClick: (DialogInterface, which: Int) -> Unit
) : MaterialAlertDialogBuilder(context) {

    init {
        val customName = args.tag.customName

        setTitle(context.getString(R.string.exit_no_save_title))
        setMessage(if (args.isNew) {
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
