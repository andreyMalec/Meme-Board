package com.proj.memeboard.ui.main.user

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.R
import com.proj.memeboard.di.Injectable
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.ui.main.detail.MemeDetailActivity
import com.proj.memeboard.ui.main.home.MemeAdapter
import com.proj.memeboard.util.MemeSharer
import kotlinx.android.synthetic.main.fragment_user.*
import javax.inject.Inject

class UserFragment : Fragment(), MemeAdapter.MemeAction, Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: UserViewModel by viewModels {
        viewModelFactory
    }

    private val adapter = MemeAdapter(this)

    private var firstLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(R.drawable.icon).apply(RequestOptions.circleCropTransform()).into(userImage)

        setViewModelListeners()
        initToolBar()

        initRecycler()
    }

    private fun setViewModelListeners() {
        viewModel.memes?.observe(viewLifecycleOwner, Observer { memes ->
            adapter.submitList(memes)
            if (firstLoad) {
                viewModel.isLoading.value = false
                firstLoad = false
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) showProgress()
            else hideProgress()
        })

        viewModel.loadError.observe(viewLifecycleOwner, Observer { error ->
            if (error) showLoadError()
        })
    }

    private fun showProgress() {
        errorText.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showLoadError() {
        if (adapter.currentList.isEmpty())
            errorText.visibility = View.VISIBLE

        val snackbar = Snackbar.make(root, getString(R.string.memes_load_error), Snackbar.LENGTH_LONG)
        snackbar.anchorView = activity?.findViewById(R.id.bottom_nav_view)
        snackbar.setBackgroundTint(ContextCompat.getColor(this.requireContext(), R.color.colorError))
        snackbar.show()
    }

    private fun initToolBar() {
        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        toolbarLayout.isTitleEnabled = false
        toolbar.overflowIcon?.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorAccent
            ),
            PorterDuff.Mode.SRC_ATOP
        )
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        setHasOptionsMenu(true)
    }

    private fun initRecycler() {
        memeRecycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        memeRecycler.adapter = adapter
        (memeRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    override fun onMemeShareClick(meme: Meme) {
        MemeSharer(requireContext()).send(meme)
    }

    override fun onMemeFavoriteClick(meme: Meme, isFavorite: Boolean) {
        val updatedMeme = Meme(
            meme.id,
            meme.title,
            meme.description,
            isFavorite,
            meme.createdDate,
            meme.photoUrl,
            meme.author
        )
        viewModel.toggleFavorite(updatedMeme)
    }

    override fun onMemeDetailClick(meme: Meme, vararg transitionOptions: Pair<View, String>) {
        activity?.let {
            val intent = MemeDetailActivity.getExtraIntent(requireContext(), meme)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it, *transitionOptions)

            startActivity(intent, options.toBundle())
        }
    }
}
