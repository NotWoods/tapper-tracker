package com.tigerxdaphne.tappertracker.pages

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.NfcDisabledFragmentBinding

class NfcDisabledFragment : Fragment() {

    private var binding: NfcDisabledFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NfcDisabledFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }


}
