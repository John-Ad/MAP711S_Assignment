package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class Task {

    @SerializedName("Task_ID")
    private var taskID: Int = 0

    @SerializedName("Name")
    private var name: String = ""

    @SerializedName("Description")
    private var description: String = ""

    @SerializedName("Username")
    private var username: String = ""

    @SerializedName("Completed")
    private var completed: Int = 0

    public fun getTaskID(): Int {
        return this.taskID
    }

    public fun getName(): String {
        return this.name
    }

    public fun getDescription(): String {
        return this.description
    }

    public fun getUsername(): String {
        return this.username
    }

    public fun getCompleted(): Int {
        return this.completed
    }

    public fun isCompleted(): Boolean {
        return this.completed == 1
    }
}