package hr.davor_news.factory.adapters.screen_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.davor_news.factory.databinding.DisplayingAllArticlesOneArticleElementBinding
import hr.davor_news.factory.fragments.displaying_all_articles.DisplayingAllArticlesViewModel
import hr.davor_news.factory.model.local_database.Article
import hr.davor_news.factory.model.remote_source.NetworkArticle

class DisplayingAllArticlesRecyclerAdapter (
    val context : Context,
    var listOfArticles : List<Article> = listOf()
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() , IOnArticlesInRecyclerChangedListener {

    var viewModelReference : DisplayingAllArticlesViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val articleBindingElement = DisplayingAllArticlesOneArticleElementBinding.inflate(LayoutInflater.from(context),parent,false)
        return NewsViewHolder(articleBindingElement)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsViewHolder).bind(position)
    }
    override fun getItemCount(): Int {
        return listOfArticles.count()
    }
    inner class NewsViewHolder(private val articleBindingElement : DisplayingAllArticlesOneArticleElementBinding) : RecyclerView.ViewHolder(articleBindingElement.root){
        fun bind(position: Int){
            articleBindingElement.viewModel = viewModelReference
            articleBindingElement.article = listOfArticles[position]
            articleBindingElement.positionOfArticlePicked = position
            articleBindingElement.executePendingBindings()
        }
    }


    override fun changeArticles(newListOfArticles : List<Article>){
        listOfArticles = newListOfArticles
        notifyDataSetChanged()
    }
}