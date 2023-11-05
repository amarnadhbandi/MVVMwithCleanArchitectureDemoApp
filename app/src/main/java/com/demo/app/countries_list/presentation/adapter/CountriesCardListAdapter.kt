package com.demo.app.countries_list.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.demo.app.countries_list.data.local.entity.CountryEntity
import com.demo.app.databinding.CountryRowBinding
import com.demo.app.utils.Constants.NUMBER_30
import com.demo.app.utils.Constants.NUMBER_ZERO

/**
 * [RecyclerView.Adapter]
 */

class CountryListAdapter :
    ListAdapter<CountryEntity, CountryListAdapter.CountryViewHolder>(CountryListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CountryRowBinding.inflate(inflater, parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = getItem(position)
        holder.bind(country)
    }

    inner class CountryViewHolder(private val binding: CountryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvCountryName = binding.name
        private val tvCountryCode = binding.code
        private val tvCountryCapital = binding.capital

        private fun ellipsize(name: String?, region: String?): String {
            val ellipseStr = ".., $region"
            return if (name?.length ?: NUMBER_ZERO > NUMBER_30) {
                "${name?.substring(0, 30)}$ellipseStr"
            } else {
                "$name, $region"
            }
        }

        fun bind(data: CountryEntity) {
            tvCountryName.text = ellipsize(data.name, data.region)
            tvCountryCode.text = data.code
            tvCountryCapital.text = data.capital
        }
    }
}

class CountryListDiffCallback : DiffUtil.ItemCallback<CountryEntity>() {
    override fun areItemsTheSame(oldItem: CountryEntity, newItem: CountryEntity): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: CountryEntity, newItem: CountryEntity): Boolean {
        return oldItem == newItem
    }
}
