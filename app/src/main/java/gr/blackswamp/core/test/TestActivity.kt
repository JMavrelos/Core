package gr.blackswamp.core.test

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import gr.blackswamp.core.R
import gr.blackswamp.core.databinding.ActivityTestBinding
import gr.blackswamp.core.views.ControlledTextWatcher
import gr.blackswamp.core.views.SpinnerAdapter
import kotlin.random.Random

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private lateinit var tw1: ControlledTextWatcher
    private lateinit var tw2: ControlledTextWatcher
    private val spinner by lazy {
        SpinnerAdapter(binding.spinner, listOf(1, 2, 3), R.array.numbers)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.action.setOnClickListener(this::doStuff)
        tw1 = ControlledTextWatcher(binding.text, this::textChanged)
        tw2 = ControlledTextWatcher(binding.editText, this::textChanged, afterChange = this::afterChange)
        spinner.setOnSelectionListener {
            textChanged("$it", 0, 0, 0)
        }
    }

    private fun afterChange(editable: Editable?) {
        editable?.append("|")
    }

    private fun textChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        val current = binding.output.text
        val new = "$current\n$text"
        binding.output.text = new
    }

    private fun doStuff(v: View?) {
        spinner.selectedItem = Random.nextInt(4)
    }
}