package id.my.avzy.oneway.api

import id.my.avzy.oneway.model.ApiResponse
import id.my.avzy.oneway.model.PostDetail
import id.my.avzy.oneway.model.StrapiPosts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

typealias ListPostResponse = ApiResponse<StrapiPosts>
typealias PostDetailResponse = ApiResponse<PostDetail>

interface ApiService {
    @GET("/strapi/posts")
    fun getPosts(): Call<ListPostResponse>

    @GET("/strapi/posts/{slug}")
    fun getPostBySlug(@Path("slug") slug: String): Call<PostDetailResponse>
}