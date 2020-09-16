package hr.davor_news.factory.model.local_database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PositionOfArticleOpened(
    @PrimaryKey
    var positionId : Int = 1,
    var positionOfArticlePicked : Int = 0
) : RealmObject()