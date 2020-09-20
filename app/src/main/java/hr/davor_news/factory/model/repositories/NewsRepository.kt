package hr.davor_news.factory.model.repositories

import android.util.Log
import hr.bagy94.android.base.repository.BaseRepository
import hr.davor_news.android.common.error.AppErrorHandler
import hr.davor_news.factory.model.local_database.Article
import hr.davor_news.factory.model.local_database.PositionOfArticleOpened
import hr.davor_news.factory.model.remote_source.INewsAPI
import hr.davor_news.factory.model.remote_source.NetworkArticle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

class NewsRepository(errorHandler: AppErrorHandler,private val newsApi :INewsAPI, private val realmInstance : Realm) : BaseRepository(errorHandler){
    fun getArticlesFromNetwork() = newsApi.getNews()
    fun saveOrUpdateLocalArticles(articles: List<NetworkArticle>){
        realmInstance.executeTransaction {
            val results : RealmResults<Article> = it.where(Article::class.java).findAll()
            if(results.isNullOrEmpty()){
                val articlesToSave =  makeNewArticleObjects(articles)
                saveOrUpdateArticlesToDatabase(articlesToSave,it)
            }else{
                for((index,member) in articles.withIndex()){
                    results[index]?.author = member.author
                    results[index]?.description = member.description
                    results[index]?.publishedAt = member.publishedAt
                    results[index]?.title = member.title
                    results[index]?.url = member.url
                    results[index]?.urlToImage = member.urlToImage
                }
            }
        }
    }
    fun saveOrUpdatePickedArticlePosition(position : Int){
        realmInstance.executeTransaction {
            val results : PositionOfArticleOpened? = it.where(PositionOfArticleOpened::class.java).findFirst()
            if(results == null)
                it.copyToRealmOrUpdate(PositionOfArticleOpened(positionOfArticlePicked = position))
            else{
                results.positionOfArticlePicked = position
            }
        }
    }
    fun getArticles() : Observable<RealmResults<Article>> {
        return Observable.just(realmInstance.where(Article::class.java).findAll())
    }
    fun getPositionOfArticleOpened() :  Observable<RealmResults<PositionOfArticleOpened>> = Observable.just(realmInstance.where(PositionOfArticleOpened::class.java).findAll())
    private fun makeNewArticleObjects(articles: List<NetworkArticle>) : List<Article>{
        val tempList = mutableListOf<Article>()
        for((index,member) in articles.withIndex()){
            tempList.add(Article(
                articleId = index,
                author = member.author,
                description = member.description,
                publishedAt = member.publishedAt,
                title = member.title,
                url = member.url,
                urlToImage = member.urlToImage
            ))
        }
        return tempList
    }
    private fun saveOrUpdateArticlesToDatabase(articlesToSaveOrUpdate : List<Article>, realm : Realm){
        for(member in articlesToSaveOrUpdate){
            realm.copyToRealmOrUpdate(member)
        }
    }

}
