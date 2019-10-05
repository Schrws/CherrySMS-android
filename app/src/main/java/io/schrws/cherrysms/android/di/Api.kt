package io.schrws.cherrysms.android.di

import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("/default/CherrySMS")
    fun postMessage(@Body body: JsonObject): Single<RequestBody>
}