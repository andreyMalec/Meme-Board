package com.proj.memeboard.ui.main.home

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.proj.memeboard.localDb.MemeData
import com.proj.memeboard.localDb.MemesDatabase
import com.proj.memeboard.model.memeRepo.MemeRepoProvider

class MemeViewModel(app: Application): AndroidViewModel(app) {
    private val repo = MemeRepoProvider.memeRepo
    private val db = MemesDatabase.getInstance(app.applicationContext)!!

    private val memesPageSize = 10

    val refreshMemes = MutableLiveData(false)
    val dump: LiveData<Boolean>

    val memes: LiveData<PagedList<MemeData>>
    val isLoading = MutableLiveData(true)
    val loadError = MutableLiveData(false)

    private var firstLoad = true

    init {
        val config = PagedList.Config.Builder().apply {
            setPageSize(memesPageSize)
            setEnablePlaceholders(false)
            setPrefetchDistance(memesPageSize / 2)
        }.build()

        val callback = MemeBoundaryCallback{
            loadMemes()
        }

        memes = LivePagedListBuilder(db.memesDataDao().factory(), config)
            .setBoundaryCallback(callback)
            .build()

        dump = Transformations.switchMap(refreshMemes) {
            if (db.memesDataDao().isEmpty() && !firstLoad)
                loadMemes()
            else
                db.memesDataDao().deleteAll()

            MutableLiveData(false)
        }
    }

    private fun loadMemes() {
        isLoading.value = true
        firstLoad = false

        repo.getMemes { memesResult ->
            loadError.value =
                if (memesResult.isSuccess) {
                    memesResult.getOrNull()?.let { memes ->
                        db.memesDataDao().insertAll(memes.map { it.convert() })
                        false
                    } ?: true
                } else
                    true

            isLoading.value = false
        }
    }
}