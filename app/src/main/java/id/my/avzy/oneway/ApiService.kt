package id.my.avzy.oneway

import id.my.avzy.oneway.data.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/strapi/posts")
    fun getPosts(): Call<ApiResponse>
}