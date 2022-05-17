package com.valentines.connection.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Client() : Parcelable {
    @SerializedName("Client_ID")
    var clientID = 0

    @SerializedName("Name")
    var name = ""

    constructor(clientID: Int, name: String) : this() {
        this.clientID = clientID
        this.name = name
    }

    constructor(parcel: Parcel) : this() {
        clientID = parcel.readInt()
        name = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(clientID)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Client> {
        override fun createFromParcel(parcel: Parcel): Client {
            return Client(parcel)
        }

        override fun newArray(size: Int): Array<Client?> {
            return arrayOfNulls(size)
        }
    }
}