package com.sas.productstest.presentation.fetch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sas.productstest.databinding.FragmentFetchBinding
import com.sas.productstest.presentation.comon.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FetchFragment : Fragment() {
    private var _binding: FragmentFetchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FetchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFetchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFetch.setOnClickListener {
            viewModel.fetchProducts()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.fetchProducts()
        }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchState.collect { state ->
                    when (state) {
                        is UiState.Idle -> {
                            binding.btnFetch.isEnabled = true
                            binding.progressBar.visibility = View.GONE
                            binding.layoutError.visibility = View.GONE
                            binding.btnFetch.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.btnFetch.isEnabled = false
                            binding.progressBar.visibility = View.VISIBLE
                            binding.layoutError.visibility = View.GONE
                            binding.btnFetch.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.btnFetch.isEnabled = true
                            binding.progressBar.visibility = View.GONE
                            binding.layoutError.visibility = View.GONE
                            //TODO: Навігацію до екрану зі списком продуктів
                            viewModel.resetState()
                        }

                        is UiState.Error -> {
                            binding.btnFetch.isEnabled = true
                            binding.progressBar.visibility = View.GONE
                            binding.layoutError.visibility = View.VISIBLE
                            binding.tvError.text = state.message
                            binding.btnFetch.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
