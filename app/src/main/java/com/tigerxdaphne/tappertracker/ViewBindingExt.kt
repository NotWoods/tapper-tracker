package com.tigerxdaphne.tappertracker

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @param viewBindingFactory Used to bind the fragment view
 */
private class ViewBindingLazy<VB : ViewBinding>(
    private val viewBindingFactory: (View) -> VB
) : ReadOnlyProperty<Fragment, VB>, DefaultLifecycleObserver {
    private var cached: VB? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        val binding = cached
        return if (binding == null) {
            val lifecycle = thisRef.viewLifecycleOwner.lifecycle
            if (!lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
            }

            viewBindingFactory(thisRef.requireView()).also {
                cached = it
                lifecycle.addObserver(this)
            }
        } else {
            binding
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cached = null
    }
}

fun <VB : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> VB
) : ReadOnlyProperty<Fragment, VB> {
    return ViewBindingLazy(viewBindingFactory)
}
