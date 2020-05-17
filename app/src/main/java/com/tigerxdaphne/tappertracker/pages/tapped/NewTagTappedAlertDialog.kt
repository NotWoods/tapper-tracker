package com.tigerxdaphne.tappertracker.pages.tapped

import android.content.Context
import android.nfc.Tag
import androidx.navigation.NavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.db.TappedTag
import com.tigerxdaphne.tappertracker.pages.edit.EditFragmentArgs
import org.threeten.bp.LocalDate

/**
 * Builds an alert dialog to display when a new tag is tapped.
 * Call [show] to display the alert.
 */
class NewTagTappedAlertDialog(
    context: Context,
    navController: NavController,
    tag: Tag
) : MaterialAlertDialogBuilder(context) {

    init {
        setTitle(context.getString(R.string.new_tag_title))
        setMessage(context.getString(R.string.new_tag_message))

        setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }
        setPositiveButton(context.getString(R.string.save)) { dialog, _ ->
            val today = LocalDate.now()
            val newTappedTag = TappedTag(
                id = tag.id,
                lastSet = today,
                reminder = today.plusWeeks(1)
            )

            val args = EditFragmentArgs(newTappedTag, isNew = true)
            navController.navigate(R.id.editFragment, args.toBundle())
            dialog.dismiss()
        }
    }
}
