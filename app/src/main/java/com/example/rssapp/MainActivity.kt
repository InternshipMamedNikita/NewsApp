package com.example.rssapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        findViewById<TextView>(R.id.inform).text = rssObject.channel!!.publications!![0].imageInfo!!.url

//        Picasso.get()
//                .load(rssObject.channel!!.publications!![0].imageInfo!!.url)
//                .into(findViewById<ImageView>(R.id.superPuperImage))

//        Picasso.get()
//                .load(rssObject.channel!!.image!!.url)
//                .into(findViewById<ImageView>(R.id.superPuperImage))


//        val image = Picasso.get()
//            .load(rssObject.channel!!.image!!.url)
//            .get()

//
//        findViewById<ImageView>(R.id.superPuperImage).setImageBitmap(image)

    }
}


@Root(name = "rss", strict = false)
data class RssObject constructor(
    @field:Element var channel: Channel? = null
)

@Root(name = "channel", strict = false)
data class Channel constructor(
    @field:Element var title: String = "",
    @field:Element var link: String = "",
    @field:Element var language: String = "",
    @field:Element var image: ChannelImage? = null,
    @field:ElementList(name = "item", inline = true) var publications: List<Publication>? = null
)

@Root(name = "image", strict = false)
data class ChannelImage constructor(
    @field:Element var url: String = "",
    @field:Element var title: String = "",
    @field:Element var link: String = ""
)

@Root(name = "item", strict = false)
class Publication constructor(
    @field:Element var title: String = "",
    @field:Element var description: String = "",
    @field:Element var guid: String = "",
    @field:Element var link: String = "",
    @field:Element var pubDate: String = "",
    @field:Element(name = "content") var imageInfo: PublicationImage? = null,
    @field:Element(name = "creator", required = false) var author: String = ""
) {
    @Root(name = "content", strict = false)
    object PublicationImage {
        @field:Attribute(name = "url") val url: String = ""
        @field:Attribute(name = "type", required = false) val type: String = ""
    }
}

//@Root(name = "media:content", strict = false)
//data class PublicationImage constructor (
//    @field:Attribute val url: String = "",
//    @field:Attribute val type: String = ""
//)


