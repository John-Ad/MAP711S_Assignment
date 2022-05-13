package com.valentines.connection.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Task() : Parcelable {

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

    constructor(
        taskID: Int,
        name: String,
        description: String,
        username: String,
        completed: Int
    ) : this() {
        this.taskID = taskID
        this.name = name
        this.description = description
        this.username = username
        this.completed = completed
    }

    constructor(parcel: Parcel) : this() {
        taskID = parcel.readInt()!!
        name = parcel.readString()!!
        description = parcel.readString()!!
        username = parcel.readString()!!
        completed = parcel.readInt()
    }

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

    fun setCompleted(completed: Int) {
        this.completed = completed
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(taskID)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(username)
        parcel.writeInt(completed)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}