package com.tigerxdaphne.tappertracker.pages.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tigerxdaphne.tappertracker.databinding.FragmentListBinding
import com.tigerxdaphne.tappertracker.viewBinding

class ListFragment : Fragment() {

    private val navController by lazy { findNavController() }
    private val viewModel by viewModels<ListViewModel>()
    private var binding by viewBinding<FragmentListBinding>()
    private lateinit var adapter: TappedTagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!viewModel.deviceSupportsNfc(requireContext())) {
            val action = ListFragmentDirections.actionListFragmentToNoNfcFragment()
            navController.navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater, container, false)
        this.binding = binding

        setupAddButton(binding.addButton)
        setupRecyclerView(binding.tagsList)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.dateChanged()) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupAddButton(addButton: ImageButton) {
        addButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToHowToTapFragment()
            navController.navigate(action)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = TappedTagAdapter(viewModel.clock)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.tags.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}
