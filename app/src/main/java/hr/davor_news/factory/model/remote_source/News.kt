package hr.davor_news.factory.model.remote_source


class News (
    val status : String,
    val source : String,
    val sortBy : String,
    val articles : List<NetworkArticle>
)