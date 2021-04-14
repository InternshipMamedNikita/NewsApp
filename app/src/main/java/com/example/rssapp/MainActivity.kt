package com.example.rssapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
    @field:Element(name = "creator", required = false) var author: String = ""
)



