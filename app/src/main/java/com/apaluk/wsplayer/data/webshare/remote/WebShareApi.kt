package com.apaluk.wsplayer.data.webshare.remote

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface WebShareApi {

    @FormUrlEncoded
    @POST("/api/salt/")
    @Headers(
        "Accept: text/xml; charset=UTF-8",
        "Content-Type: application/x-www-form-urlencoded; charset=UTF-8"
    )
    suspend fun salt(
        @Field("username_or_email") username: String
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST("/api/login/")
    @Headers(
        "Accept: text/xml; charset=UTF-8",
        "Content-Type: application/x-www-form-urlencoded; charset=UTF-8"
    )
    suspend fun login(
        @Field("username_or_email") userName: String,
        @Field("password") password: String,
        @Field("keep_logged_in") keepLoggedIn: Int = 1
    ): Response<ResponseBody>
}