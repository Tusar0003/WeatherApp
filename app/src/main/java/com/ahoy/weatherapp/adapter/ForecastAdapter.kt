package com.ahoy.weatherapp.adapter

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahoy.weatherapp.databinding.LayoutForecastItemBinding
import com.ahoy.weatherapp.model.Forecastday
import com.ahoy.weatherapp.utils.clearDecorations
import com.ahoy.weatherapp.utils.layoutInflater
import com.ahoy.weatherapp.viewmodel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class ForecastAdapter(val viewModel: HomeViewModel) :
    ListAdapter<Forecastday, ForecastViewHolder>(ForecastDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastViewHolder {
        return ForecastViewHolder(
            LayoutForecastItemBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }
}

@ExperimentalCoroutinesApi
class ForecastViewHolder(private val binding: LayoutForecastItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: HomeViewModel, data: Forecastday) {
        binding.viewModel = viewModel
        binding.forecastDay = data
        binding.executePendingBindings()
    }
}

private class ForecastDiffCallback : DiffUtil.ItemCallback<Forecastday>() {
    override fun areItemsTheSame(oldItem: Forecastday, newItem: Forecastday) =
        oldItem.date == newItem.date

    override fun areContentsTheSame(oldItem: Forecastday, newItem: Forecastday) =
        oldItem == newItem
}

@ExperimentalCoroutinesApi
@BindingAdapter(value = ["forecastViewModel", "forecastList"], requireAll = true)
fun RecyclerView.bindForecastAdapter(viewModel: HomeViewModel, data: List<Forecastday>?) {
    if (adapter == null) {
        adapter = ForecastAdapter(viewModel)
    }

    val value = data ?: emptyList()
    val forecastAdapter = adapter as? ForecastAdapter
    forecastAdapter?.submitList(value)
    clearDecorations()
}
