package gr.blackswamp.core.mvi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class CoreBottomSheetFragment<STATE : Any, COMMAND : Any, EVENT : Any, VM : CoreViewModel<STATE, COMMAND, EVENT>, V : ViewBinding> :
    BottomSheetDialogFragment() {
    abstract val vm: VM
    abstract val binding: V

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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