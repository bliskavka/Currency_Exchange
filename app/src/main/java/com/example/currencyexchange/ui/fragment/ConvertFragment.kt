package com.example.currencyexchange.ui.fragment

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
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

@AndroidEntryPoint
class ConvertFragment : Fragment(), OnItemSelectedListener {

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
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            sendEvent(ConvertScreenEvent.OnBackButtonClicked)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    binding.apply {
                        setSpinnerAdapters(it.fromCurrencyList, it.toCurrencyList)
                        fromExchangeRateText.text = it.fromCurrencyRate
                        fromAmountEdit.setText(it.fromAmount.toString())
                        toExchangeRateText.text = it.toCurrencyRate
                        toAmountEdit.setText(it.toAmount.toString())
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
                is CloseScreenWithResult -> {}
            }
        }
    }



    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun setSpinnerAdapters(fromCurrencies: List<String>, toCurrencies: List<String>) {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            fromCurrencies
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.fromCurrencySpinner.adapter = this
            binding.fromCurrencySpinner.onItemSelectedListener = this@ConvertFragment
        }

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            toCurrencies
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.toCurrencySpinner.adapter = this
            binding.toCurrencySpinner.onItemSelectedListener = this@ConvertFragment
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