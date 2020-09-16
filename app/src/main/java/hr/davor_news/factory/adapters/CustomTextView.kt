package hr.davor_news.factory.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.TextView
import hr.davor_news.factory.R

class CustomTextView : androidx.appcompat.widget.AppCompatTextView{
    constructor(context: Context) : super(context)
    constructor(context: Context, atributeSet : AttributeSet) : super(context,atributeSet)
    constructor(context: Context, atributeSet : AttributeSet, diff : Int) : super(context,atributeSet,diff)

    private val linearGradient = LinearGradient(
    0f,
    0f,
    0f,
    250f,
    resources.getColor(R.color.pure_black),
    resources.getColor(R.color.almost_white),
    Shader.TileMode.CLAMP
    )

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        paint.shader = linearGradient
    }
}