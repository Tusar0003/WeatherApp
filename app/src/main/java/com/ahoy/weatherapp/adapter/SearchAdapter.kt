package com.ahoy.weatherapp.adapter

import android.graphics.Color
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahoy.weatherapp.R
import com.ahoy.weatherapp.WeatherApp
import com.ahoy.weatherapp.databinding.LayoutSearchItemBinding
import com.ahoy.weatherapp.model.Search
import com.ahoy.weatherapp.utils.clearDecorations
import com.ahoy.weatherapp.utils.layoutInflater
import com.ahoy.weatherapp.viewmodel.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class SearchAdapter(val viewModel: SearchViewModel) :
    ListAdapter<Search, SearchViewHolder>(SearchDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        return SearchViewHolder(
            LayoutSearchItemBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
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
class SearchViewHolder(private val binding: LayoutSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: SearchViewModel, data: Search) {
        if (adapterPosition % 2 != 0) {
            binding.layoutMain.setBackgroundColor(Color.parseColor("#BED8E4"))
        }

//        binding.favouriteImageView.setOnClickListener {
//            if (data.isFavourite == true) {
//                binding.favouriteImageView.setImageResource(R.drawable.ic_baseline_favorite_24)
//            } else {
//                binding.favouriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
//            }
//        }

        binding.viewModel = viewModel
        binding.search = data
        binding.executePendingBindings()
    }
}

private class SearchDiffCallback : DiffUtil.ItemCallback<Search>() {
    override fun areItemsTheSame(oldItem: Search, newItem: Search) =
        oldItem.isFavourite == newItem.isFavourite

    override fun areContentsTheSame(oldItem: Search, newItem: Search) =
        oldItem == newItem
}

@ExperimentalCoroutinesApi
@BindingAdapter(value = ["searchViewModel", "searchList"], requireAll = true)
fun RecyclerView.bindSearchAdapter(viewModel: SearchViewModel, data: List<Search>?) {
    if (adapter == null) {
        adapter = SearchAdapter(viewModel)
    }

    val value = data ?: emptyList()
    val searchAdapter = adapter as? SearchAdapter
    searchAdapter?.submitList(value)
    clearDecorations()
}
