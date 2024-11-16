package com.practice.android.pocketmate.Model

import com.google.firebase.database.Exclude

data class BoardModel (
    var uid : String = "",
    var date: String = "",
    var title : String = "",
    var content : String = "",
    var color: Int = 0,
    var image : Int = 0,
    var agree : Int = 0,
    var disagree : Int = 0
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "date" to date,
            "title" to title,
            "content" to content,
            "color" to color,
            "image" to image,
            "agree" to agree,
            "disagree" to disagree,
        )
    }
}