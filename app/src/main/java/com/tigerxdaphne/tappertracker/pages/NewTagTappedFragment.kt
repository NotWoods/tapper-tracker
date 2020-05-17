package com.tigerxdaphne.tappertracker.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs

import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.FragmentNewTagTappedBinding
import com.tigerxdaphne.tappertracker.viewBinding

/**
 * A simple [Fragment] subclass.
 */
class NewTagTappedFragment : DialogFragment() {

    private val args by navArgs<NewTagTappedFragmentArgs>()
    private val viewModel by viewModels<NewTagTappedViewModel>()
    private var binding by viewBinding<FragmentNewTagTappedBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewTagTappedBinding.inflate(inflater, container, false)
        this.binding = binding

        binding.cancelButton.setOnClickListener {
            dialog?.cancel()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onNewTag(args.tag)
    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = requireContext().resources.getDimensionPixelSize(R.dimen.dialog_width)
        dialog?.window?.setLayout(dialogWidth, WRAP_CONTENT)
    }
}
