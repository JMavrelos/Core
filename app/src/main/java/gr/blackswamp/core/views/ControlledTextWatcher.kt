package gr.blackswamp.core.views

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import gr.blackswamp.core.extensions.value

class ControlledTextWatcher(
    private val parent: TextView,
    private val onChange: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)?,
    private val beforeChange: ((text: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
    private val afterChange: ((editable: Editable?) -> Unit)? = null
) : TextWatcher {
    init {
        parent.addTextChangedListener(this)
    }

    private var enabled = true

    var value: String?
        get() = parent.value
        set(value) {
            this.enabled = false
            parent.value = value
            this.enabled = true
        }

    var res: Int
        get() = throw RuntimeException("String resource cannot be retrieved after being set")
        set(value) {
            parent.value = parent.context.getString(value)
        }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (enabled)
            beforeChange?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (enabled)
            onChange?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        if (enabled)
            afterChange?.invoke(s)
    }

    fun detach() {
        parent.removeTextChangedListener(this)
    }
}