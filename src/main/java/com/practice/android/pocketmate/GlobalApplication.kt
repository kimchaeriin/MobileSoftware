package com.practice.android.pocketmate
import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "faf4d36560ab4d829c6f4ba0b0b6e96f")
    }
}