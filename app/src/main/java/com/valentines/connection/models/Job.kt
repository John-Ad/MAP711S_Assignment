package com.valentines.connection.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Job() : Parcelable {

    @SerializedName("Job_ID")
    private var jobID: String = ""

    @SerializedName("VIN")
    private var vin: String = ""

    @SerializedName("Name")
    private var name: String = ""

    @SerializedName("Description")
    private var description: String = ""

    @SerializedName("Completion_Date")
    private var completionDate: String = ""

    @SerializedName("Date_Added")
    private var dateAdded: String = ""

    constructor(parcel: Parcel) : this() {
        vin = parcel.readString()!!
        name = parcel.readString()!!
        description = parcel.readString()!!
        completionDate = parcel.readString()!!
        dateAdded = parcel.readString()!!
    }

    constructor(
        vin: String,
        name: String,
        description: String,
        completionDate: String,
        dateAdded: String
    ) : this() {
        this.vin = vin
        this.name = name
        this.description = description
        this.completionDate = completionDate
        this.dateAdded = dateAdded
    }

    fun getJobID(): String {
        return this.jobID
    }

    fun getVin(): String {
        return this.vin
    }

    fun getName(): String {
        return this.name
    }

    fun getDescription(): String {
        return this.description
    }

    fun getCompletionDate(): String {
        return this.completionDate
    }

    fun getDateAdded(): String {
        return this.dateAdded
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(vin)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(completionDate)
        parcel.writeString(dateAdded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Job> {
        override fun createFromParcel(parcel: Parcel): Job {
            return Job(parcel)
        }

        override fun newArray(size: Int): Array<Job?> {
            return arrayOfNulls(size)
        }
    }
}