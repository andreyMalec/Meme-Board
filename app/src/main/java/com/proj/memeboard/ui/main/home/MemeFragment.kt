package com.proj.memeboard.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.R
import kotlinx.android.synthetic.main.fragment_meme.*

class MemeFragment : Fragment() {
    private lateinit var viewModel: MemeViewModel
    private val adapter = MemeAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress()
        initSwipeRefresh()
        initRecycler()

        viewModel = ViewModelProvider(this).get(MemeViewModel::class.java)

        setViewModelListeners()
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

        viewModel.dump.observe(viewLifecycleOwner, Observer {
            //   shorturl.at/abIQ3
            //иначе не будет обрабатываться изменение swipeRefresh
        })
    }

    private fun initRecycler() {
        memeRecycler.layoutManager = StaggeredGridLayoutManager(2, 1)
        memeRecycler.adapter = adapter
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshMemes.value = !(viewModel.refreshMemes.value!!)
        }
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
}