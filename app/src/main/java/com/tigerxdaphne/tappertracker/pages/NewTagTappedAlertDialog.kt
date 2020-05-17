package com.tigerxdaphne.tappertracker.pages

import android.content.Context
import android.content.DialogInterface
import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.db.TappedTag
import com.tigerxdaphne.tappertracker.viewBinding
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

            navController.navigate(R.id.editFragment, EditFragmentArgs(newTappedTag).toBundle())
            dialog.dismiss()
        }
    }
}
