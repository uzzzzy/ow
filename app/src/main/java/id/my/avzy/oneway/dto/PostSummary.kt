package id.my.avzy.oneway.dto
import com.google.gson.annotations.SerializedName

data class PostSummary(
    @SerializedName("slug") val slug: String,
    @SerializedName("title") val title: String,
    @SerializedName("published_at") val publishedAt: String
)
