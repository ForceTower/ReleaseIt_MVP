package dev.forcetower.cubicrectangle.core.base

abstract class BaseContract {
    interface Presenter<in T> {
        fun attach(v: T)
        fun onDestroy()
    }

    interface View
}