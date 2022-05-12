package com.valentines.connection

import com.valentines.connection.models.Job
import com.valentines.connection.models.Task
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.Query

interface APIInterface {
    @GET("/jobs/all")
    fun getAllJobs(): Call<MutableList<Job>>

    @GET("/job/tasks/{data}")
    fun getTasksForJob(@Path("data") jsonString: String): Call<MutableList<Task>>
}