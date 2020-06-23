package com.tigerxdaphne.tappertracker.pages

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.FragmentHowToTapBinding
import com.tigerxdaphne.tappertracker.viewBinding

private enum class HowToTapContent(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val description: Int,
    val showSettingsButton: Boolean
) {
    NFC_DISABLED(R.drawable.onboarding_tag_disabled, R.string.nfc_disabled_title, R.string.nfc_disabled_description, true),
    ADD_TAG(R.drawable.onboarding_tag, R.string.add_tag_title, R.string.add_tag_description, false)
}

class HowToTapFragment : Fragment() {

    private var binding by viewBinding<FragmentHowToTapBinding>()

    private var lastContent: HowToTapContent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHowToTapBinding.inflate(inflater, container, false)
        this.binding = binding

        binding.button.setOnClickListener { openNfcSettings() }

        return binding.root
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
        val binding = binding!!

        if (content != lastContent) {
            binding.title.text = getString(content.title)
            binding.description.text = getString(content.description)
            binding.button.isVisible = content.showSettingsButton
        }

        // Animate the first time
        if (lastContent == null) {
            binding.onboardingPhone.animate()
                .translationYBy(resources.getDimension(R.dimen.phone_translation))
                .setDuration(1200L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(null)

            when (content) {
                HowToTapContent.ADD_TAG -> {
                    binding.onboardingTagEffect.animate()
                        .alpha(1f)
                        .setStartDelay(1000L)
                        .setDuration(400L)
                        .setListener(null)
                }
                HowToTapContent.NFC_DISABLED -> {
                    val disabledIcon = getDrawable(binding.root.context, content.icon)
                    binding.onboardingTag.setImageDrawable(disabledIcon)
                }
            }
        } else if (content != lastContent) {
            val tagIcon = getDrawable(binding.root.context, content.icon)
            binding.onboardingTag.setImageDrawable(tagIcon)

            binding.onboardingTagEffect.animate().cancel()
            binding.onboardingTagEffect.alpha = when (content) {
                HowToTapContent.ADD_TAG -> 1f
                HowToTapContent.NFC_DISABLED -> 0f
            }
        }

        lastContent = content
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
