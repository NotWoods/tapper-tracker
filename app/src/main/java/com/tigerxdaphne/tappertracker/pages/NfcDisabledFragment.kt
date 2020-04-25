package com.tigerxdaphne.tappertracker.pages

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.NfcDisabledFragmentBinding
import java.util.logging.Logger

private const val OPEN_NFC_SETTINGS = 10

class NfcDisabledFragment : Fragment() {

    private var binding: NfcDisabledFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = NfcDisabledFragmentBinding.inflate(inflater, container, false)
        this.binding = binding

        binding.button.setOnClickListener {
            openNfcSettings()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun openNfcSettings() {
        if (SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(Settings.Panel.ACTION_NFC)
            startActivityForResult(intent, OPEN_NFC_SETTINGS)
        } else {
            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
            startActivityForResult(intent, OPEN_NFC_SETTINGS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OPEN_NFC_SETTINGS) {
            // TODO
            Log.i("NfcDisabledFragment", "done with NFC settings")
        }
    }
}
