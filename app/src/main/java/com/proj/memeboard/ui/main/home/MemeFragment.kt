package com.proj.memeboard.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.R
import kotlinx.android.synthetic.main.fragment_meme.*

class MemeFragment : Fragment() {
    private lateinit var viewModel: MemeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress()
        initSwipeRefresh()

        viewModel = ViewModelProvider(this).get(MemeViewModel::class.java)

        viewModel.memes.observe(viewLifecycleOwner, Observer { result ->
            hideProgress()

            when {
                result.isSuccess -> {
                    val memes = result.getOrNull()
                }
                result.isFailure -> { showLoadError() }
            }
        })
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
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
        Snackbar.make(root, getString(R.string.memes_load_error), Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(this.requireContext(), R.color.colorError)).show()
    }
}
