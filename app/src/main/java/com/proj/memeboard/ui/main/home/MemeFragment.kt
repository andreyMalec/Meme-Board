package com.proj.memeboard.ui.main.home

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.R
import com.proj.memeboard.di.Injectable
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.util.MemeSharer
import kotlinx.android.synthetic.main.fragment_meme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MemeFragment : Fragment(), MemeAdapter.MemeAction, Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MemeViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var searchView: SearchView
    private val adapter = MemeAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meme, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_home_menu, menu)

        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        initSearchView()

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initSearchView() {
        searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
            .setBackgroundResource(android.R.color.transparent)
        searchView.queryHint = getString(R.string.search_hint)

        initSearchViewListener()
    }

    private fun initSearchViewListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText

                return false
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModelListeners()
        initToolBar()

        initSwipeRefresh()
        initRecycler()
    }

    private fun setViewModelListeners() {
        viewModel.memes.observe(viewLifecycleOwner, Observer { memes ->
            adapter.submitList(memes)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) showProgress()
            else hideProgress()
        })

        viewModel.isLoadError.observe(viewLifecycleOwner, Observer { error ->
            if (error) showLoadError()
        })
    }

    private fun showProgress() {
        errorText.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        swipeRefresh.isRefreshing = false
        progressBar.visibility = View.GONE
    }

    private fun showLoadError() {
        if (adapter.currentList.isEmpty())
            errorText.visibility = View.VISIBLE

        val root = (activity as AppCompatActivity).findViewById<View>(R.id.root)
        Snackbar.make(root, getString(R.string.memes_load_error), Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this.requireContext(), R.color.colorError))
            .setTextColor(ContextCompat.getColor(this.requireContext(), R.color.colorWhite))
            .show()
    }

    private fun initToolBar() {
        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorLightBackground
            )
        )
        toolbarLayout.isTitleEnabled = false
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_activity_main)
        setHasOptionsMenu(true)
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshMemes()
        }
    }

    private fun initRecycler() {
        memeRecycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        memeRecycler.adapter = adapter
        (memeRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    override fun onMemeShareClick(meme: Meme) {
        MemeSharer(requireContext()).send(meme)
    }

    override fun onMemeFavoriteClick(meme: Meme) {
        viewModel.toggleFavorite(meme)
    }

    override fun onMemeDetailClick(meme: Meme, vararg transitionOptions: Pair<View, String>) {
        viewModel.onDetailClick(meme, transitionOptions[0])
    }
}
