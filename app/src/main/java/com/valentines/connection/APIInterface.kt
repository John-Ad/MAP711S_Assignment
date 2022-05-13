package com.valentines.connection

import com.google.gson.JsonObject
import com.valentines.connection.models.*
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @GET("/jobs/all")
    fun getAllJobs(): Call<MutableList<Job>>

    @GET("/jobs/employee/{data}")
    fun getAllJobsForEmployee(@Path("data") jsonString: String): Call<MutableList<Job>>

    @GET("/job/tasks/completed/{data}")
    fun getTasksForJobComplete(@Path("data") jsonString: String): Call<MutableList<Task>>

    @GET("/job/tasks/incomplete/{data}")
    fun getTasksForJobIncomplete(@Path("data") jsonString: String): Call<MutableList<Task>>

    @GET("/employees/names")
    fun getEmployeeNames(): Call<MutableList<Employee>>

    @GET("/clients/details")
    fun getClientDetails(): Call<MutableList<ClientDetails>>

    @POST("/login")
    fun login(@Body json: JsonObject): Call<PostResponse>

    @POST("/jobs/add")
    fun addJob(@Body json: JsonObject): Call<PostResponse>

    @POST("/job/tasks/add")
    fun addTask(@Body json: JsonObject): Call<PostResponse>

    @POST("/job/task/mark-as-complete")
    fun markTaskAsComplete(@Body json: JsonObject): Call<PostResponse>

    @POST("/job/task/mark-as-incomplete")
    fun markTaskAsIncomplete(@Body json: JsonObject): Call<PostResponse>
}