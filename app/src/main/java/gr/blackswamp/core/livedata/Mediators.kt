@file:Suppress("unused")

package gr.blackswamp.core.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

interface Mediator {
    fun clearSources()
}

class Mediator2<T1, T2, R>(
    private val liveData1: LiveData<T1>,
    private val liveData2: LiveData<T2>,
    private val composed: (value1: T1?, value2: T2?) -> R
) : MediatorLiveData<R>(), Mediator {
    init {
        this.addSource(liveData1) {
            this.postValue(composed.invoke(it, liveData2.value))
        }
        this.addSource(liveData2) {
            this.postValue(composed.invoke(liveData1.value, it))
        }
    }

    override fun clearSources() {
        this.removeSource(liveData1)
        this.removeSource(liveData2)
    }
}

class Mediator3<T1, T2, T3, R>(
    private val liveData1: LiveData<T1>,
    private val liveData2: LiveData<T2>,
    private val liveData3: LiveData<T3>,
    private val composed: (value1: T1?, value2: T2?, value3: T3?) -> R
) : MediatorLiveData<R>(), Mediator {
    init {
        this.addSource(liveData1) {
            this.postValue(composed.invoke(it, liveData2.value, liveData3.value))
        }
        this.addSource(liveData2) {
            this.postValue(composed.invoke(liveData1.value, it, liveData3.value))
        }
        this.addSource(liveData3) {
            this.postValue(composed.invoke(liveData1.value, liveData2.value, it))
        }
    }

    override fun clearSources() {
        this.removeSource(liveData1)
        this.removeSource(liveData2)
        this.removeSource(liveData3)
    }
}

class Mediator4<T1, T2, T3, T4, R>(
    private val liveData1: LiveData<T1>,
    private val liveData2: LiveData<T2>,
    private val liveData3: LiveData<T3>,
    private val liveData4: LiveData<T4>,
    private val composed: (value1: T1?, value2: T2?, value3: T3?, value4: T4?) -> R
) : MediatorLiveData<R>(), Mediator {
    init {
        this.addSource(liveData1) {
            this.postValue(composed.invoke(it, liveData2.value, liveData3.value, liveData4.value))
        }
        this.addSource(liveData2) {
            this.postValue(composed.invoke(liveData1.value, it, liveData3.value, liveData4.value))
        }
        this.addSource(liveData3) {
            this.postValue(composed.invoke(liveData1.value, liveData2.value, it, liveData4.value))
        }
        this.addSource(liveData4) {
            this.postValue(composed.invoke(liveData1.value, liveData2.value, liveData3.value, it))
        }
    }

    override fun clearSources() {
        this.removeSource(liveData1)
        this.removeSource(liveData2)
        this.removeSource(liveData3)
        this.removeSource(liveData4)
    }
}

class Mediator5<T1, T2, T3, T4, T5, R>(
    private val liveData1: LiveData<T1>,
    private val liveData2: LiveData<T2>,
    private val liveData3: LiveData<T3>,
    private val liveData4: LiveData<T4>,
    private val liveData5: LiveData<T5>,
    private val composed: (value1: T1?, value2: T2?, value3: T3?, value4: T4?, value5: T5?) -> R
) : MediatorLiveData<R>(), Mediator {
    init {
        this.addSource(liveData1) {
            this.postValue(
                composed.invoke(
                    it,
                    liveData2.value,
                    liveData3.value,
                    liveData4.value,
                    liveData5.value
                )
            )
        }
        this.addSource(liveData2) {
            this.postValue(
                composed.invoke(
                    liveData1.value,
                    it,
                    liveData3.value,
                    liveData4.value,
                    liveData5.value
                )
            )
        }
        this.addSource(liveData3) {
            this.postValue(
                composed.invoke(
                    liveData1.value,
                    liveData2.value,
                    it,
                    liveData4.value,
                    liveData5.value
                )
            )
        }
        this.addSource(liveData4) {
            this.postValue(
                composed.invoke(
                    liveData1.value,
                    liveData2.value,
                    liveData3.value,
                    it,
                    liveData5.value
                )
            )
        }
        this.addSource(liveData5) {
            this.postValue(
                composed.invoke(
                    liveData1.value,
                    liveData2.value,
                    liveData3.value,
                    liveData4.value,
                    it
                )
            )
        }
    }

    override fun clearSources() {
        this.removeSource(liveData1)
        this.removeSource(liveData2)
        this.removeSource(liveData3)
        this.removeSource(liveData4)
        this.removeSource(liveData5)
    }
}