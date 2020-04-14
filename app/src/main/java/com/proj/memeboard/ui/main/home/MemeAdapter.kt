package com.proj.memeboard.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proj.memeboard.R
import com.proj.memeboard.databinding.MemeLayoutBinding
import com.proj.memeboard.domain.Meme

class MemeAdapter internal constructor(private val vm: MemeAction) :
    ListAdapter<Meme, MemeAdapter.MemeItemViewHolder>(diffUtilCallback) {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Meme>() {
            override fun areItemsTheSame(oldItem: Meme, newItem: Meme): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Meme, newItem: Meme): Boolean {
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
            binding?.favoriteLayout?.setOnClickListener { button ->
                button as CardView
                val favBtn = button.findViewById<CheckBox>(R.id.favoriteButton)
                favBtn.isChecked = !favBtn.isChecked
                binding.meme?.let {
                    vm.onMemeFavoriteClick(it, favBtn.isChecked)
                }
            }

            binding?.shareLayout?.setOnClickListener {
                binding.meme?.let {
                    vm.onMemeShareClick(it)
                }
            }

            binding?.image?.setOnClickListener {
                binding.meme?.let {
                    val p1 = Pair(binding.image as View, "image")
                    val p2 = Pair(binding.title as View, "title")
                    val p3 = Pair(binding.favoriteLayout as View, "favorite")

                    vm.onMemeDetailClick(it, p1, p2, p3)
                }
            }
        }
    }

    interface MemeAction {
        fun onMemeShareClick(meme: Meme)

        fun onMemeFavoriteClick(meme: Meme, isFavorite: Boolean)

        fun onMemeDetailClick(meme: Meme, vararg transitionOptions: Pair<View, String>)
    }
}