@file:Suppress("unused")
package gr.blackswamp.core.mvi

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import gr.blackswamp.core.logging.LogCat

abstract class CoreActivity<STATE, COMMAND, EVENT, VM : CoreViewModel<STATE, COMMAND, EVENT>, V : ViewBinding> : AppCompatActivity() {
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract val vm: VM

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract val binding: V

    @StyleRes
    open val currentTheme: Int? = null

    //<editor-fold desc="activity creation functions">
    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme?.let {
            setTheme(it)
        }
        resetTitle()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpBindings()
        initView(savedInstanceState)
        setUpListeners()
        vm.state.observe(this, this::updateState)
        vm.command.observe(this, this::newCommand)
    }

    protected open fun setUpBindings() {}

    protected open fun initView(state: Bundle?) {}

    protected open fun setUpListeners() {}

    protected open fun updateState(state: STATE) {
        LogCat.log { "new state received $state" }
    }

    protected open fun newCommand(command: COMMAND) {
        LogCat.log { "new command received $command" }
    }

    //</editor-fold>

    //<editor-fold desc="helper functions">
    private fun resetTitle() {
        try {
            val info = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            if (info.labelRes != 0) {
                setTitle(info.labelRes)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}