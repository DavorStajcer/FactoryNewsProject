package hr.davor_news.factory.news_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.bagy94.android.base.viewmodel.ScreenAdapter

class NewsActivityScreenAdapter() : ScreenAdapter() {
    val changeFragmets = MutableLiveData<Fragments>(Fragments.ALL_ARTICLES)
    val showErrorFragment = MutableLiveData<Boolean>(false)
}
