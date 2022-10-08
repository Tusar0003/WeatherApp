package com.ahoy.weatherapp.adapter

import android.graphics.Color
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahoy.weatherapp.databinding.LayoutFavouriteCityItemBinding
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.utils.clearDecorations
import com.ahoy.weatherapp.utils.layoutInflater
import com.ahoy.weatherapp.viewmodel.FavouriteViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class FavouriteCityAdapter(val viewModel: FavouriteViewModel) :
    ListAdapter<FavouriteCity, FavouriteViewHolder>(FavouriteDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteViewHolder {
        return FavouriteViewHolder(
            LayoutFavouriteCityItemBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

@ExperimentalCoroutinesApi
class FavouriteViewHolder(private val binding: LayoutFavouriteCityItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: FavouriteViewModel, data: FavouriteCity) {
        if (adapterPosition % 2 == 0) {
            binding.layoutMain.setBackgroundColor(Color.parseColor("#BED8E4"))
        }

        binding.viewModel = viewModel
        binding.favouriteCity = data
        binding.executePendingBindings()
    }
}

private class FavouriteDiffCallback : DiffUtil.ItemCallback<FavouriteCity>() {
    override fun areItemsTheSame(oldItem: FavouriteCity, newItem: FavouriteCity) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: FavouriteCity, newItem: FavouriteCity) =
        oldItem == newItem
}

@ExperimentalCoroutinesApi
@BindingAdapter(value = ["favouriteCityViewModel", "favouriteCityList"], requireAll = true)
fun RecyclerView.bindFavouriteCityAdapter(viewModel: FavouriteViewModel, data: List<FavouriteCity>?) {
    if (adapter == null) {
        adapter = FavouriteCityAdapter(viewModel)
    }

    val value = data ?: emptyList()
    val favouriteCityAdapter = adapter as? FavouriteCityAdapter
    favouriteCityAdapter?.submitList(value)
    clearDecorations()
}
