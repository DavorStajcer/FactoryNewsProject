package hr.davor_news.factory.model.remote_source

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface INewsAPI {
    @GET("v1/articles?source=bbc-news&sortBy=top&apiKey=6946d0c07a1c4555a4186bfcade76398")
    fun getNews() : Observable<News>
}