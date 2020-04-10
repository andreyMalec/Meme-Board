package com.proj.memeboard.ui.main.home

import androidx.paging.PagedList
import com.proj.memeboard.localDb.MemeData
import com.proj.memeboard.localDb.MemesDatabase
import com.proj.memeboard.model.memeRepo.MemeRepo

class MemeBoundaryCallback(private val loadMemes: () -> Unit): PagedList.BoundaryCallback<MemeData>() {
    override fun onZeroItemsLoaded() {
        loadMemes()
    }

    override fun onItemAtEndLoaded(itemAtEnd: MemeData) {

    }
}