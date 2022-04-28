package com.valentines.connection

import com.valentines.connection.models.Job
import retrofit2.http.GET
import retrofit2.Call

interface APIInterface {
    @GET("/jobs/all")
    fun getAllJobs(): Call<List<Job>>
}