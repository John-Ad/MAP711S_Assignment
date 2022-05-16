package com.valentines.connection.models

import com.google.gson.annotations.SerializedName

class Car(vin: String, color: String, brand: String, name: String) {

    @SerializedName("VIN")
    var vin = vin

    @SerializedName("Color")
    var color = color

    @SerializedName("Brand")
    var brand = brand

    @SerializedName("Name")
    var name = name

}