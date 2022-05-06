package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class Job {

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

    constructor(
        vin: String,
        name: String,
        description: String,
        completionDate: String,
        dateAdded: String
    ) {
        this.vin = vin
        this.name = name
        this.description = description
        this.completionDate = completionDate
        this.dateAdded = dateAdded
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
}