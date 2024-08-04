package id.my.avzy.oneway.data

import com.google.gson.annotations.SerializedName


data class ApiResponse<T> (
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("result") var result: T? = null
)