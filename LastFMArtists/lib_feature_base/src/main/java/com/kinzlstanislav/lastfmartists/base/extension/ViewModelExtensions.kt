package com.kinzlstanislav.lastfmartists.base.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> ViewModelProvider.get() = get(T::class.java)
