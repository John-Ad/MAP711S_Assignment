package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class Client(clientID: Int, name: String) {
    @SerializedName("Client_ID")
    var clientID = clientID

    @SerializedName("Name")
    var name = name
}