package hr.davor_news.factory.model.repositories


import android.util.Log
import hr.bagy94.android.base.repository.BaseRepository
import hr.davor_news.android.common.error.AppErrorHandler
import hr.davor_news.factory.model.local_database.Article
import hr.davor_news.factory.model.local_database.ArticleData
import hr.davor_news.factory.model.remote_source.INewsAPI
import hr.davor_news.factory.model.remote_source.NetworkArticle
import hr.davor_news.factory.model.remote_source.News
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.realm.*

class NewsRepository(errorHandler: AppErrorHandler,private val newsApi :INewsAPI, private val realmConfiguration : RealmConfiguration) : BaseRepository(errorHandler){

    fun shouldNetworkCallBeMade() : Boolean{
        var shouldMakeNetworkCall = false
        Realm.getInstance(realmConfiguration).executeTransaction{
            val timeOfLastNetworkCall : ArticleData? = it.where(ArticleData::class.java).equalTo("dataId",1.toInt()).findFirst()
            if(timeOfLastNetworkCall == null)
                shouldMakeNetworkCall = true
            else if((System.currentTimeMillis() - timeOfLastNetworkCall.timeOfSaving) > 5*1000)
                shouldMakeNetworkCall = true
        }
        return shouldMakeNetworkCall
    }

    fun getArticlesFromNetwork(): Flowable<News> =
        newsApi.getNews()
            .switchMap { news ->
                return@switchMap Flowable.just(news)
            }.doOnNext {
                saveOrUpdateLocalArticleData(it.articles)
            }

   private fun saveOrUpdateLocalArticleData(articles: List<NetworkArticle>){
       Realm.getInstance(realmConfiguration).executeTransaction{
            val data : ArticleData? = it.where(ArticleData::class.java).findFirst()
           if(data == null){
               val realmListOfArticles = RealmList<Article>()
               for((index,member) in articles.withIndex()){
                   Log.i("checkShould","This is loaded. Size of articles arry from network -> ${articles.size}") //size is 10
                   realmListOfArticles.add(
                       it.createObject(Article::class.java,Article(
                           articleId = index + 1,
                           author = member.author,
                           description = member.description ,
                           publishedAt = member.publishedAt,
                           title = member.title,
                           url = member.url,
                           urlToImage = member.urlToImage
                       ))
                   )
                   Log.i("checkShould","This is not loaded")
               }
               it.createObject(ArticleData::class.java,ArticleData(
                   dataId = 1,
                   timeOfSaving = System.currentTimeMillis(),
                   articles = realmListOfArticles,
               ))
               Log.i("checkShould","Realm objects made.")
           }else{
               for((index,member) in articles.withIndex()){
                   updateArticle(data.articles[index]!!,member)
               }
                data.timeOfSaving = System.currentTimeMillis()
            }
           it.close()
        }
       Log.i("checkShould","Articles saved in database.")
   }

    private fun updateArticle(article : Article, networkArticle: NetworkArticle){
        article.author = networkArticle.author
        article.description = networkArticle.description
        article.publishedAt = networkArticle.publishedAt
        article.title = networkArticle.title
        article.url = networkArticle.url
        article.urlToImage = networkArticle.urlToImage
    }


   fun getDatabaseObservable(): Flowable<RealmList<Article>> = Flowable.create(object : FlowableOnSubscribe<RealmList<Article>>{  //metoda .asFlowable() mi ne radi iz nekog razloga pa sam napisao cijelu metodu, error kaze :
        override fun subscribe(emitter: FlowableEmitter<RealmList<Article>>?) {                                              // "Cannot access class 'io.reactivex.Flowable'. Check your module classpath for missing or conflicting dependencies"
            val realmInstance = Realm.getInstance(realmConfiguration)                                                    //Mozda ima veze sa necime iz dependencies.gradle datoteke ?
            val observableRealmResults : RealmResults<ArticleData> = realmInstance.where(ArticleData::class.java).findAll()
            val realmResultsChangeListener = object : RealmChangeListener<RealmResults<ArticleData>>{
                override fun onChange(t: RealmResults<ArticleData>) {
                    Log.i("checkShould","Realm results changed.")
                    emitter?.onNext(t.first()?.articles)
                }
            }
            observableRealmResults.addChangeListener(realmResultsChangeListener)
            emitter?.setDisposable(Disposable.fromRunnable(object : Runnable{
                override fun run() {
                    observableRealmResults.removeChangeListener(realmResultsChangeListener)
                    realmInstance.close()
                }
            }))
            Log.i("checkShould","Emitting first realm result.")
            if(!observableRealmResults.isNullOrEmpty())
                emitter?.onNext(observableRealmResults.first()?.articles)
            else
                emitter?.onNext(RealmList(Article(),Article()))
        }
    },BackpressureStrategy.LATEST)
}
