package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class Employee(username: String) {

    @SerializedName("Username")
    var username: String = username
}