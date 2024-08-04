package id.my.avzy.oneway.data
import com.google.gson.annotations.SerializedName

data class PostSummary(
    @SerializedName("slug") val slug: String,
    @SerializedName("title") val title: String,
    @SerializedName("published_at") val publishedAt: String
)
