@file:Suppress("unused")

package gr.blackswamp.core.views

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import gr.blackswamp.core.extensions.enabled

class SpinnerAdapter<T>(private val spinner: Spinner, private val values: List<T>, @ArrayRes entries: Int) :
    ArrayAdapter<SpinnerAdapter.SpinnerItem<T>>(spinner.context, android.R.layout.simple_spinner_item, buildItems(spinner.context, values, entries)),
    Spinner.OnSpinnerItemSelected {
    constructor(spinner: Spinner, values: Array<T>, @ArrayRes entries: Int) : this(spinner, values.toList(), entries)

    companion object {
        private fun <T> buildItems(
            ctx: Context,
            values: List<T>,
            resource: Int
        ): List<SpinnerItem<T>> {
            val text = ctx.resources.getStringArray(resource)
            if (text.size != values.size)
                throw IllegalArgumentException("Array $resource does not contain the same amount of elements as the values provided")
            return values.mapIndexed { idx, value ->
                SpinnerItem(value, text[idx])
            }
        }
    }

    var enabled: Boolean
        get() = spinner.enabled
        set(value) {
            spinner.enabled = value
        }

    private var listener: ((T?) -> Unit)? = null

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = this
        spinner.setOnSpinnerItemSelectedListener(this)

    }

    fun setOnSelectionListener(listener: (T?) -> Unit) {
        this.listener = listener
    }

    @Suppress("UNCHECKED_CAST")
    var selectedItem: T
        get() {
            val item = spinner.selectedItem as SpinnerItem<*>
            return item.id as T
        }
        set(value) {
            val pos = values.indexOf(value)
            if (pos != spinner.selectedItemPosition) {
                spinner.selectedItemPosition = pos
            }
        }

    data class SpinnerItem<T>(val id: T, val text: String) {
        override fun toString(): String = text
    }

    override fun onItemSelected(position: Int?) {
        if (this.enabled)
            listener?.invoke(position?.let { values[it] })
    }

}
