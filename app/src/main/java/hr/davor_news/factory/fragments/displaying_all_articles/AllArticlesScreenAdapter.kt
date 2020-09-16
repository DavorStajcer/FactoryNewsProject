package hr.davor_news.factory.fragments.displaying_all_articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.bagy94.android.base.viewmodel.ScreenAdapter
import hr.davor_news.factory.model.local_database.Article

class AllArticlesScreenAdapter : ScreenAdapter() {
    val listOfArticles = MutableLiveData<List<Article>>()
    val changeFragments = MutableLiveData<Boolean>()

}