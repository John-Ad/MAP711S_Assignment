package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class PostResponse {

    @SerializedName("status")
    private var status: String = ""

    constructor(status: String) {
        this.status = status
    }

    fun getStatus(): String {
        return this.status
    }
}