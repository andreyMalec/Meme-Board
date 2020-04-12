package com.proj.memeboard.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proj.memeboard.R
import com.proj.memeboard.databinding.MemeLayoutBinding
import com.proj.memeboard.localDb.MemeData

class MemeAdapter internal constructor(private val vm: MemeAction) :
    ListAdapter<MemeData, MemeAdapter.MemeItemViewHolder>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<MemeData>() {
            override fun areItemsTheSame(oldItem: MemeData, newItem: MemeData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MemeData, newItem: MemeData): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.title == newItem.title &&
                        oldItem.description == newItem.description &&
                        oldItem.createdDate == newItem.createdDate &&
                        oldItem.isFavorite == newItem.isFavorite &&
                        oldItem.photoUrl == newItem.photoUrl
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: MemeLayoutBinding = MemeLayoutBinding.inflate(inflater, parent, false)
        return MemeItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MemeItemViewHolder, position: Int) {
        val meme = getItem(position)

        holder.binding?.meme = meme
    }

    inner class MemeItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: MemeLayoutBinding? = androidx.databinding.DataBindingUtil.bind(view)

        init {
            binding?.favoriteLayout?.setOnClickListener {
                it as CardView
                val favBtn = it.findViewById<CheckBox>(R.id.favoriteButton)
                favBtn.isChecked = !favBtn.isChecked
                vm.onMemeFavoriteClick(binding.meme, favBtn.isChecked)
            }

            binding?.shareLayout?.setOnClickListener {
                vm.onMemeShareClick(binding.meme)
            }

            binding?.image?.setOnClickListener {
                vm.onMemeDetailClick(binding.meme, it, binding.title, binding.favoriteLayout)
            }
        }
    }

    interface MemeAction {
        fun onMemeShareClick(meme: MemeData?)

        fun onMemeFavoriteClick(meme: MemeData?, isFavorite: Boolean)

        fun onMemeDetailClick(meme: MemeData?, imageView: View, titleView: View, favoriteView: View)
    }
}