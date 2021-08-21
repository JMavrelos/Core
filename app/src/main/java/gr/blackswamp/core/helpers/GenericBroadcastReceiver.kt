@file:Suppress("unused")
package gr.blackswamp.core.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class GenericBroadcastReceiver(private val receiver: (Context, Intent) -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        receiver.invoke(context, intent)
    }
}