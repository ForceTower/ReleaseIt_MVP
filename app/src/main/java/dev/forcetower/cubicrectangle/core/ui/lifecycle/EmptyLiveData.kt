package dev.forcetower.cubicrectangle.core.ui.lifecycle

import androidx.lifecycle.LiveData

class EmptyLiveData<T> private constructor() : LiveData<T>() {

    companion object {
        fun <T> create(): LiveData<T> {
            return EmptyLiveData()
        }
    }
}