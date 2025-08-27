package com.seif.tiptimebyseif

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.seif.tiptimebyseif.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var total: Double = 0.0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            binding.editTextServiceCost.setText(savedInstanceState.getString("service_cost", ""))
            binding.radioGroupRating.check(savedInstanceState.getInt("rating", -1))
            binding.switchRoundUp.isChecked = savedInstanceState.getBoolean("round_up", true)
            binding.textViewResult.text = savedInstanceState.getString("result", "Tip Amount: $0.00")
            binding.textViewResult.visibility = savedInstanceState.getInt("result_visibility", View.GONE)
            total = savedInstanceState?.getDouble("total_cost") ?: 0.0
            if (total != 0.0) {
                binding.textViewResult.visibility = android.view.View.VISIBLE
                binding.textViewResult.text = "Tip is ${String.format("%.2f", total)}"
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonCalculate.setOnClickListener {
            if (binding.editTextServiceCost.text?.isBlank() == true) {
                binding.textViewResult.visibility = android.view.View.GONE
            } else {
                val amount = binding.editTextServiceCost.text.toString().toDoubleOrNull()

                val tipPercentage = when (binding.radioGroupRating.checkedRadioButtonId) {
                    R.id.radioButtonAmazing -> 20.0
                    R.id.radioButtonGood -> 18.0
                    else -> 15.0
                }

                if (amount != null) {
                    var tip = amount * (tipPercentage / 100)
                    if (binding.switchRoundUp.isChecked) {
                        tip = kotlin.math.round(tip)
                    }
                    total = tip
                    binding.textViewResult.visibility = android.view.View.VISIBLE
                    binding.textViewResult.text = "Tip is ${String.format("%.2f", tip)}"
                }

                Snackbar.make(binding.root, "Reset Everything", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reset") {
                        binding.editTextServiceCost.text?.clear()
                        binding.textViewResult.visibility = android.view.View.GONE
                        total = 0.0
                        binding.radioGroupRating.clearCheck()
                        binding.switchRoundUp.isChecked = true
                    }
                    .setBackgroundTint(getColor(R.color.blue))
                    .setTextColor(getColor(R.color.colorPrimary))
                    .setActionTextColor(getColor(R.color.red))
                    .show()
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("total_cost", total)
        outState.putString("service_cost", binding.editTextServiceCost.text.toString())
        outState.putInt("rating", binding.radioGroupRating.checkedRadioButtonId)
        outState.putBoolean("round_up", binding.switchRoundUp.isChecked)
        outState.putString("result", binding.textViewResult.text.toString())
        outState.putInt("result_visibility", binding.textViewResult.visibility)
    }
}
