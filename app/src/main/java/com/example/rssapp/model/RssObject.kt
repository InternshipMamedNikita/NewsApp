package com.example.rssapp.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class RssObject (
        @field:Element var channel: Channel = Channel()
)