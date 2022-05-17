package com.example.valentines_garage.client_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.valentines_garage.R
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.models.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CarAddFragment : Fragment() {

    var clientID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get args
        clientID = requireArguments().getInt("clientID")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_add, container, false)
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        setLoading(false)

        // set button onClicks
        requireView().findViewById<Button>(R.id.btn_car_save).setOnClickListener {
            getUserInput()
        }
        requireView().findViewById<Button>(R.id.btn_car_back).setOnClickListener {
            goBack()
        }
    }

    //----   GET USER INPUT   ----
    private fun getUserInput() {
        val view = requireView()

        val vin = view.findViewById<EditText>(R.id.edt_car_vin).text.toString()
        val brand = view.findViewById<EditText>(R.id.edt_car_brand).text.toString()
        val name = view.findViewById<EditText>(R.id.edt_car_name).text.toString()
        val color = view.findViewById<EditText>(R.id.edt_car_color).text.toString()

        when {
            vin.isEmpty() -> {
                showToast("Enter a vin for the car"); return
            }
            brand.isEmpty() -> {
                showToast("Enter the cars brand"); return
            }
            name.isEmpty() -> {
                showToast("Enter the cars name"); return
            }
            color.isEmpty() -> {
                showToast("Enter the cars color"); return
            }
        }

        // convert to json
        var jsonData: JsonObject = JsonObject()
        jsonData.addProperty("vin", vin)
        jsonData.addProperty("cid", this.clientID)
        jsonData.addProperty("color", color)
        jsonData.addProperty("brand", brand)
        jsonData.addProperty("name", name)

        // send request
        addCar(jsonData)
    }

    //----   CLEAR INPUT   ----
    private fun clearInput() {
        requireView().findViewById<EditText>(R.id.edt_car_vin).setText("")
        requireView().findViewById<EditText>(R.id.edt_car_brand).setText("")
        requireView().findViewById<EditText>(R.id.edt_car_name).setText("")
        requireView().findViewById<EditText>(R.id.edt_car_color).setText("")
    }

    //----   ADD CLIENT   ----
    private fun addCar(data: JsonObject) {
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call = apiInterface.addCar(data)

        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {
                var res: PostResponse? = response.body()

                if (res != null) {
                    clearInput()
                    showToast(res.getStatus())
                } else {
                    showToast("Failed to add car, try again later" + response.body().toString())
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                setLoading(false)
                showToast("Failed to add car, try again later" + t.message)
                call.cancel()
            }
        })
    }

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val view: View? = view  // property access of getView()
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_car_add)
        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    //----   GO TO PREV FRAG   ----
    private fun goBack() {
        requireActivity().onBackPressed()
    }

}