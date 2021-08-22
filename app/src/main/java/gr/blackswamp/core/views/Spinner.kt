package gr.blackswamp.core.views

import android.content.Context
import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatSpinner
import gr.blackswamp.core.logging.LogCat

class Spinner @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.spinnerStyle, mode: Int = -1, popupTheme: Resources.Theme? = null) :
    AppCompatSpinner(context, attrs, defStyleAttr, mode, popupTheme), AdapterView.OnItemSelectedListener {
    private var lastProcessedPosition = -1
    private var listener: OnSpinnerItemSelected? = null

    init {
        lastProcessedPosition = 0
        super.setOnItemSelectedListener(this)
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        throw Throwable("Please use `setOnSpinnerItemSelectedListener` ")
    }

    fun setSelectedItemPosition(position: Int) {
        lastProcessedPosition = position
        setSelection(position)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        LogCat.log { "current $lastProcessedPosition updating to $position" }
        if (lastProcessedPosition != position) {
            listener?.onItemSelected(position)
        }
        lastProcessedPosition = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        listener?.onItemSelected(null)
        lastProcessedPosition = -1
    }

    override fun onSaveInstanceState(): Parcelable {
        val ss = PositionSavedState(super.onSaveInstanceState())
        ss.position = lastProcessedPosition
        return ss
    }


    fun setOnSpinnerItemSelectedListener(listener: OnSpinnerItemSelected) {
        this.listener = listener
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is PositionSavedState) {
            super.onRestoreInstanceState(state.superState)
            lastProcessedPosition = state.position
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    class PositionSavedState : BaseSavedState {
        var position: Int = -1

        constructor(superState: Parcelable?) : super(superState)
        constructor(incoming: Parcel?) : super(incoming) {
            position = incoming?.readInt() ?: -1
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(position)
        }

        @Suppress("PropertyName", "unused")
        @JvmField
        val CREATOR: Parcelable.Creator<PositionSavedState> = object : Parcelable.Creator<PositionSavedState> {
            override fun createFromParcel(source: Parcel): PositionSavedState {
                return PositionSavedState(source)
            }

            override fun newArray(size: Int): Array<PositionSavedState?> {
                return arrayOfNulls(size)
            }
        }
    }


    interface OnSpinnerItemSelected {
        fun onItemSelected(position: Int?)

    }

}