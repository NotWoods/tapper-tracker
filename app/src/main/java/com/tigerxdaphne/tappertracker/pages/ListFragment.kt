package com.tigerxdaphne.tappertracker.pages

import android.nfc.NfcAdapter
import android.nfc.NfcManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.FragmentListBinding
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var nfcAdapter: NfcAdapter
    private var binding: FragmentListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)
        // NFC not supported on the device
        if (nfcAdapter == null) {
            val action = ListFragmentDirections.actionListFragmentToNoNfcFragment()
            findNavController().navigate(action)

            return null
        }
        this.nfcAdapter = nfcAdapter

        val binding = FragmentListBinding.inflate(inflater, container, false)
        binding.addButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToHowToTapFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
