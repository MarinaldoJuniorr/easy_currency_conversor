package com.example.easy_currency_conversor

import CurrencyAdapter
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.easy_currency_conversor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var update = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tieBelow.isEnabled = false

        spinnerList()
        observeInputs()


        binding.btnSwap.setOnClickListener {
            invertButton()
            indicativeRateConversionUpdate()
        }

        binding.btnClean.setOnClickListener {
            clearInputs()
        }

        indicativeRateConversionUpdate()
    }

    private fun spinnerList() {
        val currencies = listOf(
            CurrencyItem(R.drawable.ic_br, R.string.brazil_currency),
            CurrencyItem(R.drawable.ic_canada, R.string.canada_currency),
            CurrencyItem(R.drawable.ic_china, R.string.china_currency),
            CurrencyItem(R.drawable.ic_denmark, R.string.denmark_currency),
            CurrencyItem(R.drawable.ic_new_zealand, R.string.new_zealand_currency),
            CurrencyItem(R.drawable.ic_poland, R.string.poland_currency),
            CurrencyItem(R.drawable.ic_euro, R.string.euro_currency),
            CurrencyItem(R.drawable.ic_swiss, R.string.swiss_currency),
            CurrencyItem(R.drawable.ic_usa, R.string.usa_currency),
            CurrencyItem(R.drawable.ic_british, R.string.uk_currency)
        )

        val currencyAdapter = CurrencyAdapter(this, currencies)
        binding.spAbove.adapter = currencyAdapter
        binding.spBelow.adapter = currencyAdapter
        indicativeRateConversionUpdate()
    }

    private fun clearInputs() {
        binding.tieAbove.text?.clear()
        binding.tieBelow.text?.clear()
    }

    private fun invertButton() {
        val flagOne = binding.spAbove.selectedItemPosition
        val flagTwo = binding.spBelow.selectedItemPosition
        val edtOne = binding.tieAbove.text.toString()
        val edtTwo = binding.tieBelow.text.toString()

        binding.spAbove.setSelection(flagTwo)
        binding.spBelow.setSelection(flagOne)
        binding.tieAbove.setText(edtTwo)
        binding.tieBelow.setText(edtOne)

    }

    private fun observeInputs() {
        binding.tieAbove.doOnTextChanged { text, _, _, _ ->
            if (update) return@doOnTextChanged
            showingConversion(text?.toString().orEmpty())
        }

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                showingConversion(binding.tieAbove.text?.toString().orEmpty())
                indicativeRateConversionUpdate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        binding.spAbove.onItemSelectedListener = spinnerListener
        binding.spBelow.onItemSelectedListener = spinnerListener

    }

    private fun showingConversion(raw: String) {
        val from = getSelectedCode(binding.spAbove) // ex.: "USD"
        val to = getSelectedCode(binding.spBelow) // ex.: "BRL"

        if (raw.isBlank()) {
            update = true
            binding.tieBelow.setText("")
            update = false
            return
        }

        val amount = raw.replace(',', '.').toDoubleOrNull() ?: 0.0
        val result = ExchangeRates.convert(amount, from, to)

        if (!result.isNaN()) {
            val decimals = when (to) {
                "EUR" -> 0
                else -> 2
            }
            val formatted = "%.${decimals}f".format(result)
            if (binding.tieBelow.text?.toString() != formatted) {
                update = true
                binding.tieBelow.setText(formatted)
                update = false
            }
        } else {
            update = true
            binding.tieBelow.setText("")
            update = false
        }
    }

    private fun getSelectedCode(spinner: Spinner): String {
        val ctx = spinner.context
        val item = spinner.selectedItem
        return when (item) {
            is CurrencyItem -> ctx.getString(item.code)
            is Map<*, *> -> {
                val v = item["code"]
                if (v is Int) ctx.getString(v) else v.toString()
            }

            is Int -> ctx.getString(item)
            is String -> item
            else -> item.toString()
        }
    }

    private fun indicativeRateConversionUpdate() {
        val fromItem = binding.spAbove.selectedItem as? CurrencyItem ?: return
        val toItem = binding.spBelow.selectedItem as? CurrencyItem ?: return
        binding.tvExchangeRate.text = indicativeRateConversion(fromItem, toItem)
    }

    private fun indicativeRateConversion(from: CurrencyItem, to: CurrencyItem): String {
        val ctx = binding.root.context
        val fromCode = ctx.getString(from.code)
        val toCode = ctx.getString(to.code)

        val oneUnit = ExchangeRates.convert(1.0, fromCode, toCode)

        return ctx.getString(
            R.string.subtitle_rate_value,
            fromCode,
            oneUnit,
            toCode
        )
    }

}

