package com.example.rssapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import org.simpleframework.xml.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Completable
import rx.Observable
import rx.Scheduler
import rx.Single
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.util.async.Async
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.function.Consumer
import kotlin.concurrent.thread

interface NApi {
    @GET("rss")
    fun getRssObject() : Single<RssObject>
}

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://nplus1.ru/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

        val nApi = retrofit.create(NApi::class.java)

        val rssObject = nApi.getRssObject()
            .subscribeOn(Schedulers.io())
            .toBlocking().value()

        findViewById<TextView>(R.id.inform).text = """
        Об источнике:
            ${rssObject.channel.title}
            ${rssObject.channel.language}
            ${rssObject.channel.link}
            ${rssObject.channel.image}
        О первой статье:
            ${rssObject.channel.publications[0].title}
            ${rssObject.channel.publications[0].description}
            ${rssObject.channel.publications[0].author}
            ${rssObject.channel.publications[0].image.urlToImage}
        """.trimIndent()
    }
}

@Root(name = "rss", strict = false)
data class RssObject (
    @field:Element var channel: Channel = Channel()
)

@Root(name = "channel", strict = false)
data class Channel (
        @field:Element var title: String = "",
        @field:Element var link: String = "",
        @field:Element var language: String = "",
        @field:Element var image: Image = Image(),
        @field:ElementList(name = "item", inline = true) var publications: List<Publication> = ArrayList()
) {
    data class Image (
        @field:Element var url: String = "",
        @field:Element var title: String = "",
        @field:Element var link: String = ""
    )
}

@Root(name = "item", strict = false)
class Publication (
    @field:Element var title: String = "",
    @field:Element var description: String = "",
    @field:Element var guid: String = "",
    @field:Element var link: String = "",
    @field:Element var pubDate: String = "",
    @field:Element(name = "creator", required = false) var author: String = "",
    @field:Element(name = "content", required = false) var image: Image = Image()
) {
    data class Image (
       @field:Attribute(name = "url", required = false)
       var urlToImage: String = "",
       @field:Attribute(name = "type", required = false)
       var type: String = ""
    )
}



