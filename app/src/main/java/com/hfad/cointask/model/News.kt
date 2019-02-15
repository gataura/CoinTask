package com.hfad.cointask.model

import androidx.room.*
import com.hfad.cointask.helper.NewsConverter

@Entity(tableName = "newsData")
@TypeConverters(NewsConverter::class)
class News (

        @PrimaryKey private var id: Int = 0,
        @ColumnInfo(name = "title") private var title: String = "",
        @ColumnInfo(name = "lead") private var lead: String = "",
        @ColumnInfo(name = "thumb") private var thumb: String = "",
        @ColumnInfo(name = "published_at") private var published_at: Published? = null,
        @ColumnInfo(name = "share_url") private var share_url: String = "",
        @ColumnInfo(name = "views") private var views: Int = 0,
        @ColumnInfo(name = "type") private var type: String = "",
        @ColumnInfo(name = "author") private var author: String = "",
        @ColumnInfo(name = "author_id") private var author_id: Int = 0,
        @ColumnInfo(name = "tag") private var tag: String = "",
        @ColumnInfo(name = "badge") private var badge: Badge? = null,
        @ColumnInfo(name = "is_sponsored") private var isSponsored: Boolean = false,
        @ColumnInfo(name = "audio") private var audio: String = ""
) {



    fun getTitle(): String {
        return title
    }

    fun getId(): Int {
        return id
    }

    fun getLead(): String {
        return lead
    }

    fun getThumb(): String {
        return thumb
    }

    fun getPublished_at(): Published? {
        return published_at
    }

    fun getShare_url(): String {
        return share_url
    }

    fun getViews(): Int {
        return views
    }

    fun getType(): String {
        return type
    }

    fun getAuthor(): String {
        return author
    }

    fun getAuthor_id(): Int {
        return author_id
    }

    fun getTag(): String {
        return tag
    }

    fun getBadge(): Badge? {
        return badge
    }

    fun getIsSponsored(): Boolean {
        return isSponsored
    }

    fun getAudio(): String{
        return audio
    }

}