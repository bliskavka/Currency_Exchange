package com.example.currencyexchange.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyexchange.databinding.FragmentHomeScreenBinding
import com.example.currencyexchange.ui.adapter.BalanceEntriesAdapter
import com.example.currencyexchange.ui.model.BalanceEntryUiModel
import com.example.currencyexchange.ui.viewmodel.HomeScreenAction
import com.example.currencyexchange.ui.viewmodel.HomeScreenEvent
import com.example.currencyexchange.ui.viewmodel.HomeScreenEvent.*
import com.example.currencyexchange.ui.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.launch

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
            buttonBuy.setOnClickListener { sendEvent(OnBuyButtonPressed) }
            buttonSell.setOnClickListener { sendEvent(OnSellButtonPressed) }
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
                    entriesAdapter.updateButtons(it.balanceEntries)
                }
            }
        }

        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                is HomeScreenAction.OpenBuyDialog -> {}
                is HomeScreenAction.OpenSellDialog -> {}
                is HomeScreenAction.OpenHistoryFragment -> {}
                is HomeScreenAction.ShowMessage -> {
                    Toast.makeText(requireContext(), it.text, Toast.LENGTH_SHORT).show()
                }
            }
        }
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