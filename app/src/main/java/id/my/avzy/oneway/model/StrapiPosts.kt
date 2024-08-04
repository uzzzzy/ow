package id.my.avzy.oneway.model

import com.google.gson.annotations.SerializedName

data class StrapiPosts(
    @SerializedName("meta") var meta: Meta,
    @SerializedName("data") var data: List<PostSummary>
)
