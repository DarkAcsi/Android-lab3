package com.project.lab3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.lab3.databinding.ItemBinding
import com.project.lab3.requests.Article

class DiffCallback(
    private val oldList: List<Article>,
    private val newList: List<Article>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition == newItemPosition
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

class MainAdapter() :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    var items: List<Article> = emptyList()
        set(newValue) {
            val diffCallback = DiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)

        binding.clItem.setOnClickListener { showMore(binding.clItem.tag as Article, binding) }
        binding.tvName.setOnClickListener { showMore(binding.clItem.tag as Article, binding) }
        binding.btnShowMore.setOnClickListener { showMore(binding.clItem.tag as Article, binding) }

        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            clItem.tag = item
            tvName.tag = item
            btnShowMore.tag = item
            tvName.text = item.title

            ivImage.visibility = View.GONE
            tvDetailedDescription.visibility = View.GONE
            tvLink.visibility = View.GONE
            tvData.visibility = View.GONE
            btnShowMore.rotation = 0f

            if (!item.description.isNullOrBlank()) {
                tvDescription.visibility = View.VISIBLE
                tvDescription.text = item.description
            }

            if (!item.image_url.isNullOrBlank()) {
                Glide.with(holder.binding.root.context)
                    .load(item.image_url)
                    .into(ivImage)
            }
            if (!item.content.isNullOrBlank()) tvDetailedDescription.text = item.content
            if (!item.link.isNullOrBlank()) tvLink.text = item.link
            if (!item.pubDate.isNullOrBlank()) tvData.text = item.pubDate
        }
    }

    private fun showMore(item: Article, binding: ItemBinding) {
        with(binding) {
            if (btnShowMore.rotation == 0f) {
                if (item.image_url != null) ivImage.visibility = View.VISIBLE
                if (!item.content.isNullOrBlank()) tvDetailedDescription.visibility = View.VISIBLE
                if (!item.link.isNullOrBlank()) tvLink.visibility = View.VISIBLE
                if (!item.pubDate.isNullOrBlank()) tvData.visibility = View.VISIBLE
                btnShowMore.rotation = 180f
            } else {
                ivImage.visibility = View.GONE
                tvDetailedDescription.visibility = View.GONE
                tvLink.visibility = View.GONE
                tvData.visibility = View.GONE
                btnShowMore.rotation = 0f
            }
        }
    }

    class MainViewHolder(
        val binding: ItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

}