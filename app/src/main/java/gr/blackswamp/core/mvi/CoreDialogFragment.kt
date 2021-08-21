@file:Suppress("unused")

package gr.blackswamp.core.mvi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import gr.blackswamp.core.R

abstract class CoreDialogFragment<STATE : Any, COMMAND : Any, EVENT : Any, VM : CoreViewModel<STATE, COMMAND, EVENT>, V : ViewBinding> :
    DialogFragment() {
    abstract val vm: VM
    abstract val binding: V
    protected open val cancelable: Boolean = true

    @StringRes
    protected open val positive: Int = android.R.string.ok

    @StringRes
    protected open val negative: Int = android.R.string.cancel

    @StringRes
    protected open val neutral: Int = 0

    @DrawableRes
    protected open val background: Int = R.drawable.bg_dialog

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = binding.root
        initView(savedInstanceState)
        setUpListeners()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.state.observe(viewLifecycleOwner, this::updateState)
        vm.command.observe(viewLifecycleOwner, this::newCommand)
        setUpObservers(vm)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    protected open fun initView(state: Bundle?) {}

    protected open fun setUpListeners() {}

    protected open fun setUpObservers(vm: VM) {}

    protected open fun updateState(state: STATE) {}

    protected open fun newCommand(command: COMMAND) {}

    /**
     * short hand method to add observers faster and avoid problems with lifecycle owner
     */
    protected fun <D> LiveData<D>.observe(observer: ((D?) -> Unit)) {
        this.observe(viewLifecycleOwner, {
            observer.invoke(it)
        })
    }
}