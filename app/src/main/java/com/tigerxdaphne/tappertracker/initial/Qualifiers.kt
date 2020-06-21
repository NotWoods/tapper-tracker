package com.tigerxdaphne.tappertracker.initial

import androidx.lifecycle.ProcessLifecycleOwner
import org.koin.core.qualifier.qualifier

/**
 * Qualifier to get a coroutine scope corresponding to the process.
 */
val processScope = qualifier<ProcessLifecycleOwner>()
