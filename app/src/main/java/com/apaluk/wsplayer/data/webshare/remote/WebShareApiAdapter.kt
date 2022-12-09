package com.apaluk.wsplayer.data.webshare.remote

import okhttp3.MultipartBody

object WebShareApiAdapter {
    fun salt(username: String) = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("username_or_email", username)
        .build()
}