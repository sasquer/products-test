package com.sas.productstest.presentation.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.sas.productstest.R
import com.sas.productstest.databinding.FragmentEditProductBinding
import com.sas.productstest.domain.model.Product
import com.sas.productstest.presentation.MainActivity
import com.sas.productstest.presentation.common.UiState
import com.sas.productstest.presentation.common.applySystemBarInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProductFragment : Fragment() {
    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.applyStatusBarStyle(usePrimaryColor = true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.applySystemBarInsets()

        setupToolbar()
        observeProduct()
        observeSaveResult()

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text?.toString() ?: ""
            val description = binding.etDescription.text?.toString() ?: ""
            viewModel.saveProduct(name, description)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeProduct() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.product.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.contentLayout.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.contentLayout.visibility = View.VISIBLE
                            bindProduct(state.data)
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun bindProduct(product: Product) {
        Glide.with(this)
            .load(product.image)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .centerCrop()
            .into(binding.ivProductHeader)

        with(binding) {
            if (etName.text.isNullOrEmpty()) etName.setText(product.name)
            if (etDescription.text.isNullOrEmpty()) etDescription.setText(product.description)

            tvPrice.text = getString(R.string.price_format, product.price)
            tvBrand.text = product.brand
            tvCategory.text = product.category
            tvRating.text = product.rating.toString()
            binding.tvAvailability.text = if (product.availability)
                getString(R.string.in_stock) else getString(R.string.out_of_stock)
            tvDiscount.text = getString(R.string.discount_format, product.discount)
        }
    }

    private fun observeSaveResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveResult.collect { success ->
                    if (success) {
                        findNavController().popBackStack()
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
