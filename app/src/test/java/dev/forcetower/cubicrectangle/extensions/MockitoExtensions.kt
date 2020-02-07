package dev.forcetower.cubicrectangle.extensions

import org.mockito.Mockito

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)