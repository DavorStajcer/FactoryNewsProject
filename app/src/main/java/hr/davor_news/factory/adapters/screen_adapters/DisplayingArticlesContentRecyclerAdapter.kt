package hr.davor_news.factory.adapters.screen_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.davor_news.factory.databinding.DisplayingArticlesContentOneArticleElementBinding
import hr.davor_news.factory.model.local_database.Article

class DisplayingArticlesContentRecyclerAdapter(
    private val context : Context,
    private var listOfArticles : List<Article> = listOf()
) : RecyclerView.Adapter<DisplayingArticlesContentRecyclerAdapter.ArticleViewHolder>(), IOnArticlesInRecyclerChangedListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val bindingLayout = DisplayingArticlesContentOneArticleElementBinding.inflate(LayoutInflater.from(context),parent,false)
        return ArticleViewHolder(bindingLayout)
    }
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int {
        return listOfArticles.count()
    }
    inner class ArticleViewHolder(private val bindingLayout : DisplayingArticlesContentOneArticleElementBinding) : RecyclerView.ViewHolder(bindingLayout.root){
        fun bind(articlePosition : Int){
            bindingLayout.article = listOfArticles[articlePosition]
            bindingLayout.executePendingBindings()
        }
    }
    override fun changeArticles(newListOfArticles: List<Article>) {
        listOfArticles = newListOfArticles
        notifyDataSetChanged()
    }
}