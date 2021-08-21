package gr.blackswamp.core.mvi

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.blackswamp.core.livedata.LiveEvent
import gr.blackswamp.core.logging.LogCat

/**
 * Core class that provides helper functions for get string and a scope for coroutines
 */
abstract class CoreViewModel<STATE, COMMAND, EVENT>(app: Application) : AndroidViewModel(app) {
    private val _viewState = MutableLiveData<STATE>()
    private val _viewCommand = LiveEvent<COMMAND>()
    val state: LiveData<STATE> get() = _viewState
    val command: LiveData<COMMAND> get() = _viewCommand
    protected val initialized: Boolean
        get() = _viewState.value != null


    protected var viewState: STATE
        get() = _viewState.value ?: throw UninitializedPropertyAccessException("viewstate was queried before being set")
        set(value) {
            LogCat.log("CoreViewModel") { "MVI State $value" }
            _viewState.postValue(value!!)
        }

    protected var viewStateNow: STATE
        get() = _viewState.value ?: throw UninitializedPropertyAccessException("viewstate was queried before being set")
        set(value) {
            LogCat.log("CoreViewModel") { "MVI State $value" }
            _viewState.value = value!!
        }

    protected fun send(command: COMMAND) {
        LogCat.log("CoreViewModel") { "MVI Command $command" }
        _viewCommand.postValue(command!!)
    }

    fun process(event: EVENT) {
        LogCat.log("CoreViewModel") { "MVI Event $event" }
        processEvent(event)
    }

    protected open fun processEvent(event: EVENT) {
    }

    protected fun getString(@StringRes resId: Int): String =
        getApplication<Application>().getString(resId)

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String =
        getApplication<Application>().getString(resId, *formatArgs)
}