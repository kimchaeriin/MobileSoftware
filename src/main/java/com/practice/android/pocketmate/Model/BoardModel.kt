package com.practice.android.pocketmate.Model

import com.google.firebase.database.Exclude

data class BoardModel (
    var uid : String = "",
    var nickname: String = "",
    var title : String = "",
    var content : String = "",
    var image : Int = 0,
    var agree : Int = 0,
    var disagree : Int = 0
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "nickname" to nickname,
            "title" to title,
            "content" to content,
            "image" to image,
            "agree" to agree,
            "disagree" to disagree,
        )
    }
}