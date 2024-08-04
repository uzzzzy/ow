package id.my.avzy.oneway.data
import com.google.gson.annotations.SerializedName

data class PostDetail(
    @SerializedName("id") val id: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("views") val views: Int
)
