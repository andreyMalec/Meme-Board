package com.proj.memeboard.ui.main.home

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.R
import com.proj.memeboard.localDb.MemeData
import com.proj.memeboard.ui.main.MemeDetailActivity
import com.proj.memeboard.util.MemeSharer
import kotlinx.android.synthetic.main.fragment_meme.*

class MemeFragment : Fragment(), MemeAdapter.MemeAction {
    private lateinit var viewModel: MemeViewModel
    private val adapter = MemeAdapter(this)
    private lateinit var memes: List<MemeData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meme, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_home_menu, menu)

        val searchMenuItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
            .setImageResource(R.drawable.close)
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank())
                    adapter.submitList(memes)
                else
                    adapter.submitList(memes.filter {
                        it.title?.toLowerCase()?.contains(newText.trim().toLowerCase()) ?: false
                    })

                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MemeViewModel::class.java)

        setViewModelListeners()
        initToolBar()

        initSwipeRefresh()
        initRecycler()
    }

    private fun initToolBar() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_activity_main)
        setHasOptionsMenu(true)
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshMemes.value = !(viewModel.refreshMemes.value!!)
        }
    }

    private fun initRecycler() {
        memeRecycler.layoutManager = StaggeredGridLayoutManager(2, 1)
        memeRecycler.adapter = adapter
    }

    private fun setViewModelListeners() {
        viewModel.memes.observe(viewLifecycleOwner, Observer { memes ->
            this.memes = memes
            adapter.submitList(memes)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) showProgress()
            else hideProgress()
        })

        viewModel.loadError.observe(viewLifecycleOwner, Observer { error ->
            if (error) showLoadError()
        })

        viewModel.dump.observe(viewLifecycleOwner, Observer {
            //   shorturl.at/abIQ3
            //иначе не будет обрабатываться изменение swipeRefresh
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
        errorText.visibility = View.VISIBLE
        Snackbar.make(root, getString(R.string.memes_load_error), Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this.requireContext(), R.color.colorError))
            .show()
    }

    override fun onMemeShareClick(meme: MemeData?) {
        if (meme != null)
            MemeSharer(context!!).send(meme)
    }

    override fun onMemeDetailClick(meme: MemeData?, imageView: View, titleView: View, favoriteView: View) {
        if (meme != null) {
            val intent = MemeDetailActivity.getExtraIntent(context!!, meme)

            val p1 = androidx.core.util.Pair(imageView, "image")
            val p2 = androidx.core.util.Pair(titleView, "title")
            val p3 = androidx.core.util.Pair(favoriteView, "favorite")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, p1, p2, p3)

            startActivity(intent, options.toBundle())
        }
    }
}
