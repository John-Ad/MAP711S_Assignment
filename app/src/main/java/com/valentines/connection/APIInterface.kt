package com.valentines.connection

import com.google.gson.JsonObject
import com.valentines.connection.models.ClientDetails
import com.valentines.connection.models.Employee
import com.valentines.connection.models.Job
import com.valentines.connection.models.Task
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @GET("/jobs/all")
    fun getAllJobs(): Call<MutableList<Job>>

    @GET("/job/tasks/{data}")
    fun getTasksForJob(@Path("data") jsonString: String): Call<MutableList<Task>>

    @GET("/employees/names")
    fun getEmployeeNames(): Call<MutableList<Employee>>

    @GET("/clients/details")
    fun getClientDetails(): Call<MutableList<ClientDetails>>

    @POST("/jobs/add")
    fun addJob(@Body json: JsonObject): Call<String>
}