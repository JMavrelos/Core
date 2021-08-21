@file:Suppress("unused")

package gr.blackswamp.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton.SIZE_MINI
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import gr.blackswamp.core.R

private var miniSize: Int = 0
private var normalSize: Int = 0
private var fabMargin: Int = 0

fun FloatingActionButton.moveBy(offset: Float, max: Int) {
    val fabSize: Int = if (this.size == SIZE_MINI) {
        if (miniSize == 0) {
            miniSize = (40 * this.resources.displayMetrics.density).toInt()
        }
        miniSize
    } else {
        if (normalSize == 0) {
            normalSize = (56 * this.resources.displayMetrics.density).toInt()
        }
        normalSize
    }
    if (fabMargin == 0) {
        fabMargin = this.resources.getDimensionPixelSize(R.dimen.fab_margin)
    }

    val maxOffset = max - (fabSize / 2) - fabMargin

    val params = layoutParams as CoordinatorLayout.LayoutParams
    params.marginEnd = ((offset * maxOffset) + fabMargin).toInt()
    layoutParams = params
}

var MenuItem.visible
    get() = this.isVisible
    set(value) {
        if (this.isVisible && !value) {
            this.isVisible = false
        } else if (!this.isVisible && value) {
            this.isVisible = true
        }
    }

var View.visible
    get() = this.isVisible
    set(value) {
        if (this.isVisible && !value) {
            this.isVisible = false
        } else if (!this.isVisible && value) {
            this.isVisible = true
        }
    }

var View.enabled
    get() = this.isEnabled
    set(value) {
        if (isEnabled && !value)
            isEnabled = false
        else if (!isEnabled && value)
            isEnabled = true
    }

var TextView.value: String?
    get() = this.text?.toString()
    set(value) {
        if (this.text?.toString() ?: "" != value ?: "")
            text = value
    }

var TextView.res: Int
    get() = throw RuntimeException("String resource cannot be retrieved after being set")
    set(value) {
        this.value = this.context.getString(value)
    }

@SuppressLint("ClickableViewAccessibility")
fun EditText.onIconClickListener(block: () -> Unit) {
    this.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (right - compoundDrawables[2].bounds.width())) {
                block.invoke()
                true
            } else
                false
        } else
            false
    }
}

fun View.showSnackBar(message: String, @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_INDEFINITE) {
    Snackbar.make(this, message, duration).apply {
        if (duration == Snackbar.LENGTH_INDEFINITE) {
            setAction(android.R.string.ok) {
                dismiss()
            }
        }

    }.show()
}

fun View.hideKeyboard() {
    try {
        windowToken?.let {
            (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(it, 0)
        }
    } catch (ignored: Throwable) {
    }
}