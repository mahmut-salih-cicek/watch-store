package com.sout.custom_apple_watch.API

import com.sout.custom_apple_watch.Model.StoreModel
import io.reactivex.Observable
import retrofit2.http.GET

interface API {

    @GET("mahmut-salih-cicek/Play_Store_Picture/main/app.json")
    fun getData():Observable<List<StoreModel>>
}