package com.proj.memeboard.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proj.memeboard.databinding.MemeLayoutBinding
import com.proj.memeboard.localDb.MemeData

class MemeAdapter internal constructor(private val vm: ShareMeme):
    PagedListAdapter<MemeData, MemeAdapter.MemeItemViewHolder>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object: DiffUtil.ItemCallback<MemeData>() {
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
        holder.binding?.favoriteLayout?.setOnClickListener { it as CardView
            val favBtn = it.getChildAt(0) as CheckBox
            favBtn.isChecked = !favBtn.isChecked
        }
        holder.binding?.shareLayout?.setOnClickListener {
            vm.onMemeShareClick(meme)
        }
    }

    inner class MemeItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding: MemeLayoutBinding? = androidx.databinding.DataBindingUtil.bind(view)
    }

    interface ShareMeme {
        fun onMemeShareClick(meme: MemeData?)
    }
}