package com.tigerxdaphne.tappertracker.pages

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible

import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.FragmentHowToTapBinding
import com.tigerxdaphne.tappertracker.viewBinding
import org.koin.android.ext.android.inject

private enum class HowToTapContent(
        @DrawableRes val icon: Int,
        @StringRes val title: Int,
        @StringRes val description: Int,
        val showSettingsButton: Boolean
) {
    NFC_DISABLED(R.drawable.ic_nfc_disabled, R.string.nfc_disabled_title, R.string.nfc_disabled_description, true),
    ADD_TAG(R.drawable.ic_nfc_enabled, R.string.add_tag_title, R.string.add_tag_description, false)
}

class HowToTapFragment : Fragment() {

    private val binding by viewBinding(FragmentHowToTapBinding::bind)

    private var lastContent: HowToTapContent? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener { openNfcSettings() }
    }

    /**
     * Check NFC adapter on resume, so it is re-checked when returning from settings/quick settings
     */
    override fun onResume() {
        super.onResume()

        val nfcAdapter = requireNotNull(NfcAdapter.getDefaultAdapter(context))
        checkNfc(nfcAdapter)
    }

    /**
     * Check if the NFC adapter is enabled or disabled.
     * If disabled, the user is instructed to turn on NFC.
     * Otherwise, the normal "add a tag" instructions are shown.
     */
    private fun checkNfc(nfcAdapter: NfcAdapter) {
        val content = if (nfcAdapter.isEnabled) {
            HowToTapContent.ADD_TAG
        } else {
            HowToTapContent.NFC_DISABLED
        }
        displayContent(content)
    }

    /**
     * Display the content described in the given enum.
     */
    private fun displayContent(content: HowToTapContent) {
        if (content != lastContent) {
            val binding = binding!!
            binding.icon.setImageResource(content.icon)
            binding.title.text = getString(content.title)
            binding.description.text = getString(content.description)
            binding.button.isVisible = content.showSettingsButton
            lastContent = content
        }
    }

    /**
     * Open the Settings app.
     */
    private fun openNfcSettings() {
        val action = if (SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Panel.ACTION_NFC
        } else {
            Settings.ACTION_NFC_SETTINGS
        }
        val intent = Intent(action)
        startActivity(intent)
    }
}
