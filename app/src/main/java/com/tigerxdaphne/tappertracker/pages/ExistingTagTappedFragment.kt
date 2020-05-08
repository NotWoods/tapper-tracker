package com.tigerxdaphne.tappertracker.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.tigerxdaphne.tappertracker.databinding.FragmentExistingTagTappedBinding

class ExistingTagTappedFragment : DialogFragment() {

    private val viewModel by viewModels<ExistingTagTappedViewModel>()
    private var binding: FragmentExistingTagTappedBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExistingTagTappedBinding.inflate(inflater, container, false)
        this.binding = binding

        // STOP: stop tag then close
        // EDIT: go to edit fragment
        // RESET: reset tag then close

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.dismiss.observe(this) { event ->
            event.getContentIfNotHandled()?.let { dismiss() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
