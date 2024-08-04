package id.my.avzy.oneway.data

import com.google.gson.annotations.SerializedName


data class ApiResponse (
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
)