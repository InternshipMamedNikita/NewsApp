package com.example.rssapp.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.rssapp.R
import com.example.rssapp.model.Publication
import com.example.rssapp.model.RssObject
import java.io.File
import java.lang.Exception
import java.net.URI

class RssAdapter(
        private val newsList: List<Publication>,
        private val context: Context
) : BaseAdapter() {

    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null)
            view = inflater.inflate(R.layout.publication, parent, false)

        val publication = getItem(position) as Publication
        view?.findViewById<TextView>(R.id.header)!!.text = publication.title
        view?.findViewById<TextView>(R.id.description).text = publication.description
        publication.image.setBitmapToImageView(view?.findViewById<ImageView>(R.id.picture))
//        val image = publication.image.bitmapToImage.
//        view?.findViewById<ImageView>(R.id.picture).setImageBitmap()

        return view
    }

    override fun getItem(position: Int): Any {
        return newsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return newsList.size
    }

}