package com.example.currencyexchange.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyexchange.R
import com.example.currencyexchange.databinding.FragmentHomeScreenBinding
import com.example.currencyexchange.ui.adapter.BalanceEntriesAdapter
import com.example.currencyexchange.ui.model.BalanceEntryUiModel
import com.example.currencyexchange.ui.viewmodel.ConvertResult
import com.example.currencyexchange.ui.viewmodel.HomeScreenAction
import com.example.currencyexchange.ui.viewmodel.HomeScreenEvent
import com.example.currencyexchange.ui.viewmodel.HomeScreenEvent.*
import com.example.currencyexchange.ui.viewmodel.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel: HomeScreenViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonConvert.setOnClickListener { sendEvent(OnConvertButtonPressed) }
            buttonDeposit.setOnClickListener { sendEvent(OnDepositButtonPressed) }
            buttonHistory.setOnClickListener { sendEvent(OnHistoryButtonPressed) }
        }

        val entriesAdapter = BalanceEntriesAdapter(viewModel.state.value.balanceEntries, ::onBalanceEntryClicked)
        binding.balancesRecycler.apply {
            adapter = entriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    binding.apply {
                        textBalanceAmount.text = it.baseBalance.balance
                        textBalanceCurrency.text = it.baseBalance.currencySymbol.toString()
                    }
                    entriesAdapter.updateButtons(it.balanceEntries)
                }
            }
        }

        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                is HomeScreenAction.OpenConvertDialog -> {
                    setFragmentResultListener("convert_fragment") { _, bundle ->
                        val result = bundle.getString("result") ?: ""
                        sendEvent(OnScreenOpened())
                    }
                    parentFragmentManager.commit {
                        add(R.id.host, ConvertFragment.newInstance())
                    }
                }

                is HomeScreenAction.OpenDepositDialog -> {
                    sendEvent(OnDepositButtonPressed)
                }

                is HomeScreenAction.OpenHistoryFragment -> {}
                is HomeScreenAction.ShowMessage -> {
                    Toast.makeText(requireContext(), it.text, Toast.LENGTH_SHORT).show()
                }
            }
        }

        sendEvent(OnScreenOpened())
    }

    private fun onBalanceEntryClicked(entry: BalanceEntryUiModel) {
        viewModel.onEvent(OnBalanceEntryPressed(entry))
    }

    private fun sendEvent(event: HomeScreenEvent) {
        viewModel.onEvent(event)
    }

    companion object {
        fun newInstance(args: Bundle = bundleOf()): HomeScreenFragment {
            val fragment = HomeScreenFragment()
            if (!args.isEmpty) {
                fragment.arguments = args
            }
            return fragment
        }
    }
}