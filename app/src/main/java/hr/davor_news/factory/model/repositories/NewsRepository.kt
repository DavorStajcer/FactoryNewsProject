package hr.davor_news.factory.model.repositories


import android.util.Log
import hr.bagy94.android.base.repository.BaseRepository
import hr.davor_news.android.common.error.AppErrorHandler
import hr.davor_news.factory.model.local_database.Article
import hr.davor_news.factory.model.remote_source.INewsAPI
import hr.davor_news.factory.model.remote_source.NetworkArticle
import hr.davor_news.factory.model.remote_source.News
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration
import io.realm.RealmResults

class NewsRepository(errorHandler: AppErrorHandler,private val newsApi :INewsAPI, private val realmConfiguration : RealmConfiguration) : BaseRepository(errorHandler){

    fun getArticlesFromNetwork(): Flowable<News> =
        newsApi.getNews()
            .switchMap { news ->
                return@switchMap Flowable.just(news)
            }.doOnNext {
                saveOrUpdateLocalArticles(it.articles)
            }
   private fun saveOrUpdateLocalArticles(articles: List<NetworkArticle>){
       Realm.getInstance(realmConfiguration).executeTransactionAsync {
            val results : RealmResults<Article> = it.where(Article::class.java).findAll()
            if(results.isNullOrEmpty()){
               for((index,member) in articles.withIndex()){
                    it.createObject(Article::class.java).also { article ->
                        updateArticle(article,index + 1,member)
                    }
                }
            }else{
                for((index,member) in articles.withIndex()){
                    updateArticle(results[index]!!,null,member)
                }
            }
            it.close()
        }
    }
    private fun updateArticle(article : Article, articleId : Int?, networkArticle: NetworkArticle){
        if(articleId != null)
            article.articleId = articleId
        article.author = networkArticle.author
        article.description = networkArticle.description
        article.publishedAt = networkArticle.publishedAt
        article.title = networkArticle.title
        article.url = networkArticle.url
        article.urlToImage = networkArticle.urlToImage

    }

    fun getDatabaseObservable(): Flowable<List<Article>> = Flowable.create(object : FlowableOnSubscribe<List<Article>>{  //metoda .asFlowable() mi ne radi iz nekog razloga pa sam napisao cijelu metodu, error kaze :
        override fun subscribe(emitter: FlowableEmitter<List<Article>>?) {                                              // "Cannot access class 'io.reactivex.Flowable'. Check your module classpath for missing or conflicting dependencies"
            val realmInstance = Realm.getInstance(realmConfiguration)                                                    //Mozda ima veze sa necime iz dependencies.gradle datoteke ?
            val observableRealmResults : RealmResults<Article> = realmInstance.where(Article::class.java).findAll()
            val realmResultsChangeListener = object : RealmChangeListener<RealmResults<Article>>{
                override fun onChange(t: RealmResults<Article>) {
                    emitter?.onNext(t)
                }
            }
            observableRealmResults.addChangeListener(realmResultsChangeListener)
            emitter?.setDisposable(Disposable.fromRunnable(object : Runnable{
                override fun run() {
                    observableRealmResults.removeChangeListener(realmResultsChangeListener)
                    realmInstance.close()
                }
            }))
            emitter?.onNext(observableRealmResults)
        }
    },BackpressureStrategy.LATEST)
}
