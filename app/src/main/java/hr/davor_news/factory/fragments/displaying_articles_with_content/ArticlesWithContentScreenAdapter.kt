package hr.davor_news.factory.fragments.displaying_articles_with_content

import androidx.lifecycle.MutableLiveData
import hr.bagy94.android.base.viewmodel.ScreenAdapter
import hr.davor_news.factory.model.local_database.Article

class ArticlesWithContentScreenAdapter() : ScreenAdapter(){

     val listOfArticles = MutableLiveData<List<Article>>()
     val showNetworkErrorDialog = MutableLiveData<Boolean>()
}