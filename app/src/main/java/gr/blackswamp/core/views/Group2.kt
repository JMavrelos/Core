@file:Suppress("unused")

package gr.blackswamp.core.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import gr.blackswamp.core.extensions.enabled

class Group2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Group(context, attrs, defStyleAttr) {


    public override fun applyLayoutFeatures() {
        val parent = (this.parent as? ConstraintLayout) ?: return
        this.applyLayoutFeatures(parent)
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun applyLayoutFeatures(container: ConstraintLayout) {
        val visibility = this.visibility
        val enabled = this.isEnabled
        val elevation = if (Build.VERSION.SDK_INT >= 21)
            this.elevation
        else
            0f


        for (i in 0 until mCount) {
            val id = mIds[i]
            val view = container.getViewById(id)
            if (view != null) {
                view.visibility = visibility
                view.enabled = enabled
                if (elevation > 0.0f && Build.VERSION.SDK_INT >= 21) {
                    view.translationZ = view.translationZ + elevation
                }
            }
        }
    }
}