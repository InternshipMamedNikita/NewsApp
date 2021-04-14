package com.example.rssapp.model.api

import com.example.rssapp.model.RssObject
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Single

interface RssService {
    @GET("{rss_path}")
    fun getRssObject(@Path("rss_path") rssPath: String) : Single<RssObject>
}