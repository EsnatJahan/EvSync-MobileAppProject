package com.esa.evsync.app

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esa.evsync.app.pages.PageEnum


class AppViewModel: ViewModel() {
    private var _page = MutableLiveData<PageEnum>()
    val page : LiveData<PageEnum> get() = _page

    init {
        _page.value = PageEnum.PAGE_TASKS
    }

    fun switch_page(page: PageEnum) {
        _page.value = page
    }
}