package com.ahoy.weatherapp.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ahoy.weatherapp.R
import com.ahoy.weatherapp.databinding.FragmentSearchBinding
import com.ahoy.weatherapp.utils.*
import com.ahoy.weatherapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.message.collect {
                    showDialog {
                        setMessage(it)
                        positiveButton(getString(R.string.ok)) {}
                    }
                }
            }

            launch {
                viewModel.toastMessage.collect {
                    requireContext().toastLong(it)
                }
            }

            launch {
                binding.searchInputLayout.setStartIconOnClickListener {
                    navigateUp()
                }
            }
        }
    }
}
