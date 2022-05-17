package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class UserOverview(
    completedTasks: String,
    incompleteTasks: String,
    jobsCompleted: String,
    jobMostCompleted: String,
    jobMostIncomplete: String,
    clientMostComplete: String
) {
    @SerializedName("Completed_Tasks")
    public var completedTasks: String = completedTasks

    @SerializedName("Incomplete_Tasks")
    public var incompleteTasks: String = incompleteTasks


    @SerializedName("Jobs_Complete")
    public var jobsCompleted: String = jobsCompleted

    @SerializedName("Job_Most_Completed")
    public var jobMostCompleted: String = jobMostCompleted

    @SerializedName("Job_Most_Incomplete")
    public var jobMostIncomplete: String = jobMostIncomplete

    @SerializedName("Client_Most_Complete")
    public var clientMostComplete: String = clientMostComplete

}