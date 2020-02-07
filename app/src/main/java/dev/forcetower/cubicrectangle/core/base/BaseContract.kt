package dev.forcetower.cubicrectangle.core.base

interface BaseContract {
    interface Presenter<in T> {
        fun attach(v: T)
        fun onDestroy()
    }

    interface View
}