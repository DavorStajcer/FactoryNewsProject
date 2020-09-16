package hr.davor_news.factory.adapters.binding_adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("srcFromUrl")
fun ImageView.setImageSourceFromUrl(url : String?){
    if(url != null)
        Glide.with(this.context).load(url).centerCrop().into(this)
}
