package id.my.avzy.oneway.api

import id.my.avzy.oneway.model.SendMessage
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class SendTokenRequest(
    val token: String
)

interface FcmApi {
    @POST("/fcm/token")
    fun sendToken(@Body request: SendTokenRequest): Call<Any>
}
