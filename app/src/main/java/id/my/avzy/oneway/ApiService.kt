package id.my.avzy.oneway

import id.my.avzy.oneway.dto.ApiResponse
import id.my.avzy.oneway.dto.PostDetail
import id.my.avzy.oneway.dto.StrapiPosts
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