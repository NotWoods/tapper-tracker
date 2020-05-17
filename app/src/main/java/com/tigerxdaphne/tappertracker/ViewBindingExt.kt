package com.tigerxdaphne.tappertracker

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private class ViewBindingAutoDestroy<VB : ViewBinding>
    : ReadWriteProperty<Fragment, VB?>, DefaultLifecycleObserver {

    private var binding: VB? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB? {
        return binding
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: VB?) {
        binding = value

        thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        binding = null
    }
}

/**
 * Automatically destroys the reference when the fragment is destroyed
 */
fun <VB : ViewBinding> Fragment.viewBinding() : ReadWriteProperty<Fragment, VB?> =
    ViewBindingAutoDestroy()
