package com.mgorski.discountasciiwarehouse.asciiitemlist

import android.databinding.ObservableArrayList
import com.mgorski.discountasciiwarehouse.R
import com.mgorski.discountasciiwarehouse.model.AsciiItem
import com.mgorski.discountasciiwarehouse.model.toAsciiItem
import com.mgorski.discountasciiwarehouse.network.AsciiWarehouseService
import com.mgorski.discountasciiwarehouse.view.messages.MessagesManager
import com.mgorski.discountasciiwarehouse.view.recyclerview.pagination.PaginationRecyclerViewOnScrollListener
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class AsciiItemListViewModel(private val service: AsciiWarehouseService, private val messagesManager: MessagesManager) {

    private var query = ""

    val items = ObservableArrayList<AsciiItem>()

    init {
        loadItems()
    }

    val onLoadMoreCommand = { listener: PaginationRecyclerViewOnScrollListener -> loadItems(listener) }

    fun onQueryChangedCommand(query: String) {
        this.query = query
        items.clear()
        loadItems()
    }

    private fun loadItems(listener: PaginationRecyclerViewOnScrollListener? = null): Unit {
        service.getAsciiItems(skip = items.count(), tagsQuery = query)
                .subscribeOn(Schedulers.io())
                .map { it.map { it.toAsciiItem() } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.size > 0) {
                        items.addAll(it)
                    } else {
                        messagesManager.showMessage(R.string.no_result, R.string.retry, { loadItems(listener) })
                    }
                    listener?.loadingFinished()
                }, {
                    messagesManager.showMessage(R.string.download_error, R.string.retry, { loadItems(listener) })
                    listener?.loadingFinished()
                })
    }
}