package com.tigerxdaphne.tappertracker.pages

import android.nfc.NfcAdapter
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
import org.threeten.bp.LocalDate

class ListFragment : Fragment() {

    private val navController by lazy { findNavController() }
    private val viewModel by viewModels<ListViewModel>()
    private var binding by viewBinding<FragmentListBinding>()
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)
        // NFC not supported on the device
        if (nfcAdapter != null) {
            this.nfcAdapter = nfcAdapter
        } else {
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

    private fun setupAddButton(addButton: ImageButton) {
        addButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToHowToTapFragment()
            navController.navigate(action)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val adapter = TappedTagAdapter(LocalDate.now())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.tags.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}
