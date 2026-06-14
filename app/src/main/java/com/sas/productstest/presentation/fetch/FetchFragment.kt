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
import androidx.navigation.fragment.findNavController
import com.sas.productstest.databinding.FragmentFetchBinding
import com.sas.productstest.presentation.MainActivity
import com.sas.productstest.presentation.common.UiState
import com.sas.productstest.presentation.common.applySystemBarInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FetchFragment : Fragment() {
    private var _binding: FragmentFetchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FetchViewModel by viewModels()

    private var isLoading = false
    private var hasCachedData = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFetchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.applyStatusBarStyle(usePrimaryColor = false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            root.applySystemBarInsets()
            btnFetch.setOnClickListener { viewModel.fetchProducts() }
            btnRetry.setOnClickListener { viewModel.fetchProducts() }
            btnShowCache.setOnClickListener { navigateToProductList() }
            btnShowCacheError.setOnClickListener { navigateToProductList() }
        }

        observeState()
        observeCacheAvailability()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchState.collect { state ->
                    when (state) {
                        is UiState.Idle -> {
                            binding.btnFetch.isEnabled = true
                            binding.btnShowCache.isEnabled = true
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.errorContent.visibility = View.GONE
                            binding.mainContent.visibility = View.VISIBLE
                            binding.btnFetch.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.btnFetch.isEnabled = false
                            binding.btnShowCache.isEnabled = false
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorContent.visibility = View.GONE
                            binding.mainContent.visibility = View.VISIBLE
                            binding.btnFetch.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.btnFetch.isEnabled = true
                            binding.btnShowCache.isEnabled = true
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.errorContent.visibility = View.GONE
                            navigateToProductList()
                            viewModel.resetState()
                        }

                        is UiState.Error -> {
                            binding.btnFetch.isEnabled = true
                            binding.btnShowCache.isEnabled = true
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.mainContent.visibility = View.GONE
                            binding.errorContent.visibility = View.VISIBLE
                            binding.tvError.text = state.message
                            binding.btnFetch.visibility = View.GONE
                        }
                    }
                    updateCacheButtonsAvailability()
                }
            }
        }
    }

    private fun observeCacheAvailability() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hasCachedData.collect { hasCache ->
                    hasCachedData = hasCache
                    updateCacheButtonsAvailability()
                }
            }
        }
    }

    private fun updateCacheButtonsAvailability() {
        val visibility = if (hasCachedData && !isLoading) View.VISIBLE else View.GONE
        binding.btnShowCache.visibility = visibility
        binding.btnShowCacheError.visibility = visibility
    }

    private fun navigateToProductList() {
        val action = FetchFragmentDirections.actionFetchFragmentToProductListFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
