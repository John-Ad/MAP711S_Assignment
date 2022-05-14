package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class UserOverview {
    @SerializedName("Completed_Tasks")
    public var completedTasks: String = ""

    @SerializedName("Incomplete_Tasks")
    public var incompleteTasks: String = ""

    constructor(completedTasks: String, incompleteTasks: String) {
        this.completedTasks = completedTasks
        this.incompleteTasks = incompleteTasks
    }
}