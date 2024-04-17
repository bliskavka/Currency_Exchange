package com.example.currencyexchange.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.FragmentConvertBinding
import com.example.currencyexchange.ui.viewmodel.ConvertScreenAction.*
import com.example.currencyexchange.ui.viewmodel.ConvertScreenEvent
import com.example.currencyexchange.ui.viewmodel.ConvertViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

@AndroidEntryPoint
class ConvertFragment : Fragment() {

    private lateinit var binding: FragmentConvertBinding
    private val viewModel: ConvertViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConvertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            toolbar.setNavigationIcon(R.drawable.back_arrow_ic)
            toolbar.setNavigationOnClickListener { sendEvent(ConvertScreenEvent.OnBackButtonClicked) }
            submitButton.setOnClickListener { sendEvent(ConvertScreenEvent.OnSubmitButtonClicked) }

            fromAmountEdit.doAfterTextChanged { text ->
                if (fromAmountEdit.hasFocus()) sendEvent(ConvertScreenEvent.OnFromAmountEntered(text.toString()))
            }
            toAmountEdit.doAfterTextChanged { text ->
                if (toAmountEdit.hasFocus()) sendEvent(ConvertScreenEvent.OnToAmountEntered(text.toString()))
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            sendEvent(ConvertScreenEvent.OnBackButtonClicked)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    binding.apply {
                        setSpinnerAdapters(it.fromCurrencyList, it.toCurrencyList)
                        binding.fromCurrencySpinner.setSelection(it.fromSelectedItemId)
                        binding.toCurrencySpinner.setSelection(it.toSelectedItemId)
                        fromExchangeRateText.text = it.fromCurrencyRate
                        toExchangeRateText.text = it.toCurrencyRate
                        if (!fromAmountEdit.hasFocus()) fromAmountEdit.setText(it.fromAmount.toString())
                        if (!toAmountEdit.hasFocus()) toAmountEdit.setText(it.toAmount.toString())
                        binding.feeBannerText.isVisible = it.feeAmount.isNotEmpty()
                        binding.feeBannerText.text = String.format(resources.getString(R.string.transaction_fee_will_be_applied), it.feeAmount)
                    }
                }
            }
        }

        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                is ShowMessage -> {
                    Toast.makeText(requireContext(), it.text, Toast.LENGTH_SHORT).show()
                }

                is CloseScreen -> {
                    parentFragmentManager.commit {
                        remove(this@ConvertFragment)
                    }
                }

                is CloseScreenWithResult -> {
                    setFragmentResult("convert_fragment", bundleOf("result" to it.result))
                    parentFragmentManager.commit {
                        remove(this@ConvertFragment)
                    }
                }
            }
        }

        sendEvent(ConvertScreenEvent.OnScreenOpened())
    }

    private fun setSpinnerAdapters(fromCurrencies: List<String>, toCurrencies: List<String>) {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            fromCurrencies
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.fromCurrencySpinner.adapter = this
            binding.fromCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    sendEvent(ConvertScreenEvent.OnFromCurrencyCodeSelected(position))
                }
            }
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            toCurrencies
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.toCurrencySpinner.adapter = this
            binding.toCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    sendEvent(ConvertScreenEvent.OnToCurrencyCodeSelected(position))
                }
            }
        }
    }

    private fun sendEvent(event: ConvertScreenEvent) {
        viewModel.onEvent(event)
    }

    companion object {
        fun newInstance(args: Bundle = bundleOf()): ConvertFragment {
            val fragment = ConvertFragment()
            if (!args.isEmpty) {
                fragment.arguments = args
            }
            return fragment
        }
    }
}