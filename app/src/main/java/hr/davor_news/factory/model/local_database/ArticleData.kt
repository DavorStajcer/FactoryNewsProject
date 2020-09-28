package hr.davor_news.factory.model.local_database

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ArticleData(
    @PrimaryKey
    var dataId : Int = 1,
    var timeOfSaving : Long = 0,
    var articles : RealmList<Article> = RealmList()
) : RealmObject()