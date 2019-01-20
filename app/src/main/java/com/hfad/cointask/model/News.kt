package com.hfad.cointask.model

class News (
        private var title: String,
        private var id: Int,
        private var lead: String,
        private var thumb: String,
        private var published_at: Published,
        private var share_url: String,
        private var views: Int,
        private var type: String,
        private var author: String,
        private var author_id: Int,
        private var tag: String,
        private var badge: Badge,
        private var isSponsored: Boolean,
        private var audio: String
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

    fun getPublishedAt(): Published {
        return published_at
    }

    fun getShareUrl(): String {
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

    fun getAuthorId(): Int {
        return author_id
    }

    fun getTag(): String {
        return tag
    }

    fun getBadge(): Badge {
        return badge
    }

    fun getIsSponsored(): Boolean {
        return isSponsored
    }

    fun getAudio(): String{
        return audio
    }

}