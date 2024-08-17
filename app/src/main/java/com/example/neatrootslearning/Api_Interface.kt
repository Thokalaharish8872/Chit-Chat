package com.example.neatrootslearning

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api_Interface {

    @Headers(
        "x-rapidapi-key: d74de88367mshda962e7c604b8cdp1504a9jsn1cdd4485492c",
        "x-rapidapi-host: spotify23.p.rapidapi.com"
    )
    @GET("search")
    fun getData(@Query("q") query: String): Call<TeluguData>
}