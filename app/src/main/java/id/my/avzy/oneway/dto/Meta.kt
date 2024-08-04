package id.my.avzy.oneway.dto

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("total") var total: Int,
    @SerializedName("per_page") var perPage: Int,
    @SerializedName("last_page") var lastPage: Int,
    @SerializedName("current_page") var currentPage: Int
)
