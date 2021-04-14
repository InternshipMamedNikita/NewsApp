package com.example.rssapp.model

import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import rx.Single
import rx.schedulers.Schedulers
import java.util.ArrayList

@Root(name = "channel", strict = false)
data class Channel (
        @field:Element var title: String = "",
        @field:Element var link: String = "",
        @field:Element var language: String = "",
        @field:Element(required = false) var image: Image = Image(),
        @field:ElementList(name = "item", inline = true) var publications: List<Publication> = ArrayList()
) {
    data class Image (
            @field:Element(name = "url", required = false) var urlToImage: String = "",
            @field:Element(required = false) var title: String = "",
            @field:Element(required = false) var link: String = ""
    ) {
        var bitmapToImage: Bitmap? = null
            get() = Single.fromCallable { Picasso.get().load(urlToImage).get() }
                    .subscribeOn(Schedulers.io())
                    .toBlocking()
                    .value()
    }
}