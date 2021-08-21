@file:Suppress("unused")

package gr.blackswamp.core.views

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.ArrayRes
import gr.blackswamp.core.extensions.enabled

class SpinnerAdapter<T>(private val spinner: Spinner, private val values: List<T>, @ArrayRes entries: Int) :
    ArrayAdapter<SpinnerAdapter.SpinnerItem<T>>(spinner.context, android.R.layout.simple_spinner_item, buildItems(spinner.context, values, entries)),
    AdapterView.OnItemSelectedListener {
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
        spinner.tag = 0
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = this
        spinner.onItemSelectedListener = this

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
                spinner.tag = pos
                spinner.setSelection(pos)
            }
        }

    data class SpinnerItem<T>(val id: T, val text: String) {
        override fun toString(): String = text
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (spinner.tag != position) {
            if (this.enabled)
                listener?.invoke(values[position])
            spinner.tag = position
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        if (this.enabled)
            listener?.invoke(null)
    }

}
