package com.example.rssapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable
import rx.Scheduler
import rx.Single
import rx.schedulers.Schedulers
import java.util.ArrayList
import java.util.function.Consumer

interface Api {
    @GET("rss")
    fun getResponse() : Single<RssObject>
}

interface NApi {
    @GET("rss")
    fun getRssObject() : Observable<RssObject>

    @GET("rss/channel/item")
    fun getPublications() : Observable<List<Publication>>
}

class MainActivity : AppCompatActivity() {

    lateinit var rssObject: RssObject

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rssObject = RssObject()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://nplus1.ru/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        val nApi = retrofit.create(NApi::class.java)

        AndroidSchedulers.mainThread()
        val observable = nApi.getRssObject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val rss = observable.doOnNext { rss -> findViewById<TextView>(R.id.inform).text = rss.channel.title }


    }
}

@Root(name = "rss", strict = false)
class RssObject {

    @get:Element(name = "channel")
    @set:Element(name = "channel")
    var channel: Channel = Channel()

}

@Root(name = "channel", strict = false)
class Channel {

    @get:Element
    @set:Element
    lateinit var title: String

    @get:Element(name = "link")
    @set:Element(name = "link")
    var link: String = ""

    @get:Element(name = "language")
    @set:Element(name = "language")
    var language: String = ""

    @get:Element(name = "image")
    @set:Element(name = "image")
    var image: ImageInfo = ImageInfo()

    @get:ElementList(name = "item", inline = true)
    @set:ElementList(name = "item", inline = true)
    var publications: List<Publication> = ArrayList()

}

@Root(name = "image", strict = false)
class ImageInfo {

    @get:Element(name = "url")
    @set:Element(name = "url")
    var url: String = ""

    @get:Element(name = "title")
    @set:Element(name = "title")
    var title: String = ""

    @get:Element(name = "link")
    @set:Element(name = "link")
    var link: String = ""

}

@Root(name = "item", strict = false)
class Publication {
    @get:Element(name = "title")
    @set:Element(name = "title")
    var title: String = ""

    @get:Element(name = "description")
    @set:Element(name = "description")
    var description: String = ""

    @get:Element(name = "guid")
    @set:Element(name = "guid")
    var guid: String = ""

    @get:Element(name = "link")
    @set:Element(name = "link")
    var link: String = ""

    @get:Element(name = "pubDate")
    @set:Element(name = "pubDate")
    var pubDate: String = ""

}

