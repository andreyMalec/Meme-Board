package com.proj.memeboard.ui.main.home

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.R
import com.proj.memeboard.localDb.MemeData
import com.proj.memeboard.ui.main.home.detail.MemeDetailActivity
import com.proj.memeboard.util.MemeSharer
import kotlinx.android.synthetic.main.fragment_meme.*

class MemeFragment : Fragment(), MemeAdapter.MemeAction {
    private lateinit var viewModel: MemeViewModel
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
        searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
            .setImageResource(R.drawable.close)
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

        viewModel = ViewModelProvider(this).get(MemeViewModel::class.java)

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

        viewModel.loadError.observe(viewLifecycleOwner, Observer { error ->
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

        val snackbar = Snackbar.make(root, getString(R.string.memes_load_error), Snackbar.LENGTH_LONG)
        snackbar.anchorView = activity?.findViewById(R.id.nav_view)
        snackbar.setBackgroundTint(ContextCompat.getColor(this.requireContext(), R.color.colorError))
        snackbar.show()
    }

    private fun initToolBar() {
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

    override fun onMemeShareClick(meme: MemeData?) {
        meme?.let { MemeSharer(context!!).send(it) }
    }

    override fun onMemeFavoriteClick(meme: MemeData?, isFavorite: Boolean) {
        meme?.let {
            val updatedMeme = MemeData(
                it.id,
                it.title,
                it.description,
                isFavorite,
                it.createdDate,
                it.photoUrl,
                it.author
            )
            viewModel.updateMeme(updatedMeme)
        }
    }

    override fun onMemeDetailClick(meme: MemeData?, imageView: View, titleView: View, favoriteView: View) {
        meme?.let {
            val intent = MemeDetailActivity.getExtraIntent(requireContext(), it)

            val p1 = Pair(imageView, "image")
            val p2 = Pair(titleView, "title")
            val p3 = Pair(favoriteView, "favorite")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, p1, p2, p3)

            startActivity(intent, options.toBundle())
        }
    }
}
