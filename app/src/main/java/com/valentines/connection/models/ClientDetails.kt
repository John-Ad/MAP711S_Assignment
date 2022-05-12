package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class ClientDetails(vin: String, clientName: String, name: String) {
    @SerializedName("VIN")
    var vin = vin

    @SerializedName("Client_Name")
    var clientName = clientName

    @SerializedName("Name")
    var name = name
}

