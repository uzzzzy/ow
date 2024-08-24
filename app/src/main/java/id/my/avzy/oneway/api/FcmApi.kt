package id.my.avzy.oneway.api

import id.my.avzy.oneway.model.SendMessage
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessage
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body: SendMessage
    )
}