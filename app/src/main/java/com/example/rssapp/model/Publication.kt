package com.example.rssapp.model

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import rx.Single
import rx.schedulers.Schedulers

@Root(name = "item", strict = false)
data class Publication (
        @field:Element var title: String = "",
        @field:Element var description: String = "",
        @field:Element var link: String = "",
        @field:Element var pubDate: String = "",
        @field:Element(required = false) var guid: String = "",
        @field:Element(name = "creator", required = false) var author: String = "",
        @field:Element(name = "content", required = false) var image: Image = Image()
) {
    data class Image (
            @field:Attribute(name = "url", required = false)
            var urlToImage: String = "",
            @field:Attribute(name = "type", required = false)
            var type: String = ""
    ) {
        var bitmapToImage: Bitmap? = null
            get() = Single.fromCallable { Picasso.get().load(urlToImage).get() }
                    .subscribeOn(Schedulers.io())
                    .toBlocking()
                    .value()
        fun setBitmapToImageView(imageView: ImageView) {
            Picasso.get().load(urlToImage).fit().into(imageView)
        }
    }
}