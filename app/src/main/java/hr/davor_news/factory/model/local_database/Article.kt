package hr.davor_news.factory.model.local_database


import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey


open class Article (
    @PrimaryKey
    var articleId : Int = 1,
    var author : String = "Daca",
    var title : String = "Naslov",
    var description : String = "Opis",
    var url : String = "url",
    var urlToImage : String = "https://www.ft.com/__origami/service/image/v2/images/raw/http%3A%2F%2Fcom.ft.imagepublish.upp-prod-us.s3.amazonaws.com%2F5ecccf40-b7e5-11e9-96bd-8e884d3ea203?fit=scale-down&source=next&width=700",
    var publishedAt : String? = "PublishedAt"
) : RealmObject()


