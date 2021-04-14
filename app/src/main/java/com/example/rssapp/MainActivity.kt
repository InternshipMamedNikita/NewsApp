package com.example.rssapp

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.rssapp.model.api.RssRequest
import com.example.rssapp.presenter.RssAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val request = RssRequest
        val rssObject = request.getRssFeed("https://nplus1.ru/", "rss")

        val rssFeed = findViewById<ListView>(R.id.rss_feed)
        rssFeed.adapter = RssAdapter(rssObject.channel.publications, this)
    }
}









