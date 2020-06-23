package com.tigerxdaphne.tappertracker.pages.tapped

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.FragmentExistingTagTappedBinding
import com.tigerxdaphne.tappertracker.pages.tapped.ExistingTagTappedFragmentDirections.Companion.actionExistingTagTappedFragmentToEditFragment
import com.tigerxdaphne.tappertracker.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExistingTagTappedFragment : DialogFragment() {

    private val navController by lazy { findNavController() }
    private val args by navArgs<ExistingTagTappedFragmentArgs>()
    private val viewModel by viewModel<ExistingTagTappedViewModel> { parametersOf(args) }
    private var binding by viewBinding<FragmentExistingTagTappedBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExistingTagTappedBinding.inflate(inflater, container, false)
        this.binding = binding

        binding.dialogTitle.text = args.tag.name

        binding.stop.apply {
            isGone = args.tag.isStopped
            setOnClickListener {
                viewModel.stop()
                dismiss()
            }
        }

        binding.reset.setOnClickListener {
            viewModel.reset()
            dismiss()
        }

        binding.edit.setOnClickListener {
            val action = actionExistingTagTappedFragmentToEditFragment(args.tag, isNew = false)
            navController.navigate(action)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = requireContext().resources.getDimensionPixelSize(R.dimen.dialog_width)
        dialog?.window?.setLayout(dialogWidth, WRAP_CONTENT)
    }
}
