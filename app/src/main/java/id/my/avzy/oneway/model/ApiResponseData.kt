package id.my.avzy.oneway.model

import com.google.gson.annotations.SerializedName


data class ApiResponseData<T> (
    @SerializedName("status"  ) var status  : String,
    @SerializedName("message" ) var message : String,
    @SerializedName("result") var result : T
)