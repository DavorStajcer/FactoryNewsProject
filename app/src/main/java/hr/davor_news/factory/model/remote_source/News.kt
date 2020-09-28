package hr.davor_news.factory.model.remote_source

import hr.davor_news.factory.model.local_database.Article


class News (
    val status : String,
    val source : String,
    val sortBy : String,
    val articles : List<NetworkArticle>
)