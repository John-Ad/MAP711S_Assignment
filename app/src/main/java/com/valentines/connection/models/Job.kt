package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class Job {

    @SerializedName("VIN")
    private var vin: String = ""

    @SerializedName("Name")
    private var name: String = ""

    @SerializedName("Description")
    private var description: String = ""

    constructor(vin: String, name: String, description: String) {
        this.vin = vin
        this.name = name
        this.description = description
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
}