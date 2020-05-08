package com.tigerxdaphne.tappertracker.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs

import com.tigerxdaphne.tappertracker.R

/**
 * A simple [Fragment] subclass.
 */
class NewTagTappedFragment : DialogFragment() {

    private val args: AddTagFragmentArgs by navArgs()
    private val viewModel: NewTagTappedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.onNewTag(args.tag)

        return inflater.inflate(R.layout.fragment_new_tag_tapped, container, false)
    }

    // Read NFC
    // Check if the tag was saved
    // If saved, update options
    // If new, new tag options
}
