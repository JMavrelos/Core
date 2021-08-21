@file:Suppress("unused")
package gr.blackswamp.core.livedata

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class LiveEvent<T>(private val maxSize: Int = -1) : MutableLiveData<T>() {
    companion object {
        const val TAG = "SingleLiveEvent"
    }

    private val pending = AtomicBoolean(false)
    private val posted = AtomicBoolean(false)
    private val queue = LinkedList<T>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers())
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        super.observe(owner, {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
                Log.i(TAG, "got ${posted.get()} stack ${queue.size} ")
                if (queue.isEmpty())
                    posted.set(false)
                else
                    super.postValue(queue.pop())
            }
        })
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        if (hasActiveObservers())
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        super.observeForever {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
                Log.i(TAG, "got ${posted.get()} stack ${queue.size} ")
                if (queue.isEmpty())
                    posted.set(false)
                else
                    super.postValue(queue.removeFirst())
            }
        }
    }

    @MainThread
    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }

    override fun postValue(value: T) {
        Log.i(TAG, "posted ${posted.get()} stack ${queue.size} value $value ")
        if (posted.getAndSet(true)) {
            if (maxSize <= 0 || queue.size <= maxSize)
                queue.add(value)
        } else
            super.postValue(value)
    }

    /**
     * I just added this because for some reason I couldn't access this otherwise in tests
     */
    @Suppress("RedundantOverride")
    override fun getValue(): T? {
        return super.getValue()
    }
}