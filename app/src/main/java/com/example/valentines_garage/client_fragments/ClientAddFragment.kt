package com.example.valentines_garage.client_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.valentines_garage.R
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.models.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_add, container, false)
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        setLoading(false)

        // set button onClicks
        requireView().findViewById<Button>(R.id.btn_client_save).setOnClickListener {
            getUserInput()
        }
        requireView().findViewById<Button>(R.id.btn_client_back).setOnClickListener {
            goBack()
        }
    }

    //----   GET USER INPUT   ----
    private fun getUserInput() {
        val view = requireView()

        val name = view.findViewById<EditText>(R.id.edt_client_name).text.toString()

        when {
            name.isEmpty() -> {
                showToast("Enter a name for the client"); return
            }
        }

        // convert to json
        var jsonData: JsonObject = JsonObject()
        jsonData.addProperty("name", name)

        // send request
        addClient(jsonData)
    }

    //----   CLEAR INPUT   ----
    private fun clearInput() {
        requireView().findViewById<EditText>(R.id.edt_client_name).setText("")
    }

    //----   ADD CLIENT   ----
    private fun addClient(data: JsonObject) {
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call = apiInterface.addClient(data)

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
                    showToast("Failed to add client, try again later" + response.body().toString())
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                setLoading(false)
                showToast("Failed to add client, try again later" + t.message)
                call.cancel()
            }
        })
    }

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val view: View? = view  // property access of getView()
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_client_add)
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