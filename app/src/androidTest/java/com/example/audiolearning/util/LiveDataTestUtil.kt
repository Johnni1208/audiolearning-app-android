package com.example.audiolearning.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
@Throws(InterruptedException::class)
fun <T> LiveData<T>.getTestValue(): T {
    val data = ArrayList<Any>()
    val latch = CountDownLatch(1)

    val observer = Observer<T> {
        data.add(it as Any)
        latch.countDown()
    }

    this.removeObserver(observer)
    this.observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)

    return data[0] as T
}