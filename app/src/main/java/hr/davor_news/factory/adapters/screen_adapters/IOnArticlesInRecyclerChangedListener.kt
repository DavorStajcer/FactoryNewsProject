package hr.davor_news.factory.adapters.screen_adapters

import hr.davor_news.factory.model.local_database.Article

interface IOnArticlesInRecyclerChangedListener {
    fun changeArticles(newListOfArticles : List<Article>)
}