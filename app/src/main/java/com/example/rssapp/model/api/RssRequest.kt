package com.example.rssapp.model.api

import com.example.rssapp.model.RssObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import rx.schedulers.Schedulers

object RssRequest {
    fun getRssFeed(baseUrl: String, urlToRss: String): RssObject {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        return retrofit.create(RssService::class.java)
                .getRssObject(urlToRss)
                .subscribeOn(Schedulers.io())
                .toBlocking()
                .value()
    }
}