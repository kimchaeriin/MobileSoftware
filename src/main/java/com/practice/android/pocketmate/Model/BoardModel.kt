package com.practice.android.pocketmate.Model

data class BoardModel (
    var writer : String = "",
    var title : String = "",
    var content : String = "",
    var image : Int = 0,
    var agree : String = "",
    var disagree : String = ""
)

//<com.google.android.material.floatingactionbutton.FloatingActionButton
//android:id="@+id/fab"
//android:contentDescription="어쩌고"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:layout_gravity="bottom|end"
//android:layout_marginEnd="@dimen/fab_margin"
//android:layout_marginBottom="16dp"
//app:srcCompat="@android:drawable/ic_dialog_email" />