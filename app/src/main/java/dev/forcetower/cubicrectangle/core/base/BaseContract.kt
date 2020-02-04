package dev.forcetower.cubicrectangle.core.base

class BaseContract {
    interface Presenter<in T> {
        fun attach(v: T)
    }

    interface View
}