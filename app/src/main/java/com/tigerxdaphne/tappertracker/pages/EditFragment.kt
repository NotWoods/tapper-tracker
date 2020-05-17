package com.tigerxdaphne.tappertracker.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.tigerxdaphne.tappertracker.databinding.FragmentEditBinding
import com.tigerxdaphne.tappertracker.viewBinding

class EditFragment : Fragment() {

    private val args by navArgs<EditFragmentArgs>()
    private val viewModel by viewModels<EditViewModel>()
    private var binding by viewBinding<FragmentEditBinding>()
    private lateinit var reminderUnitAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditBinding.inflate(inflater, container, false)
        this.binding = binding

        reminderUnitAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        reminderUnitAdapter.setNotifyOnChange(false)
        binding.reminderUnitField.setAdapter(reminderUnitAdapter)

        binding.nameField.setText(args.tag.customName)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.reminderDuration.observe(viewLifecycleOwner) { reminderDuration ->
            val strings = viewModel.timeUnits.map {
                resources.getQuantityString(it.resId, reminderDuration)
            }

            reminderUnitAdapter.clear()
            reminderUnitAdapter.addAll(strings)
            reminderUnitAdapter.notifyDataSetChanged()
        }
    }
}
