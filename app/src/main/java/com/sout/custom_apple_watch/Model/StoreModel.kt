package com.sout.custom_apple_watch.Model

import com.google.gson.annotations.SerializedName

data class StoreModel(
    @SerializedName("name")
    var name : String,
    @SerializedName("icon")
    var icon : String,
    @SerializedName("ss")
    var ss :String,
    @SerializedName("link")
    var link : String,
    var discount :String
)
