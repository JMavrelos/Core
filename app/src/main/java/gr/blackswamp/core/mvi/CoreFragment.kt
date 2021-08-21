package gr.blackswamp.core.mvi

import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding

abstract class CoreFragment<STATE, COMMAND, EVENT, VM : CoreViewModel<STATE, COMMAND, EVENT>, V : ViewBinding> : Fragment() {
    abstract val vm: VM
    abstract val binding: V

    @MenuRes
    protected open val optionsMenuId: Int = 0

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(optionsMenuId > 0)
        val view = binding.root
        initView(savedInstanceState)
        setUpListeners()
        return view
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.state.observe(viewLifecycleOwner, this::updateState)
        vm.command.observe(viewLifecycleOwner, this::newCommand)
        setUpObservers()
    }

    final override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (optionsMenuId > 0)
            inflater.inflate(optionsMenuId, menu)
        else
            super.onCreateOptionsMenu(menu, inflater)
    }

    protected open fun initView(state: Bundle?) {}

    protected open fun setUpListeners() {}

    protected open fun setUpObservers() {}

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