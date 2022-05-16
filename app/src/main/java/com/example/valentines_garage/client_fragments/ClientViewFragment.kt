package com.example.valentines_garage.client_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.valentines_garage.JobFragment
import com.example.valentines_garage.R
import com.example.valentines_garage.job_related_fragments.JobAddFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.State
import com.valentines.connection.adapters.CarsListAdapter
import com.valentines.connection.adapters.JobsListAdapter
import com.valentines.connection.models.Car
import com.valentines.connection.models.Client
import com.valentines.connection.models.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientViewFragment : Fragment() {

    var client: Client? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get args
        client = requireArguments().getParcelable("client")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        init()
        if (State.getInstance().getUserType() == State.USER_ADMIN) initAdmin() else initEmployee()
    }

    private fun init() {
        requireView().findViewById<TextView>(R.id.txt_client_name).text =
            "Cars of " + this.client!!.name + ":"

        getCarsData()
    }

    //----   INIT ADMIN   ----
    private fun initAdmin() {
        setButtonListeners()
    }

    //----   INIT EMPLOYEE   ----
    private fun initEmployee() {
        // hide FAB
        requireView().findViewById<FloatingActionButton>(R.id.fab_add_car).visibility = View.GONE
    }

    //----   SET BUTTON LISTENERS   ----
    private fun setButtonListeners() {

        // set FAB listener
        val view: View = requireView()
        view.findViewById<FloatingActionButton>(R.id.fab_add_car).setOnClickListener {
            val fragment = CarAddFragment()

            var bundle = Bundle()
            bundle.putInt("clientID", this.client!!.clientID)

            fragment.arguments = bundle

            val ft = requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_frame, fragment, null)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    //----   GET CARS DATA   ----
    private fun getCarsData() {
        // show that data is loading
        setLoading(true)

        // set data
        var jsonObject = JsonObject()
        jsonObject.addProperty("clientID", this.client!!.clientID)

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call: Call<MutableList<Car>> = apiInterface.getAllClientCars(jsonObject.toString())

        call.enqueue(object : Callback<MutableList<Car>> {
            override fun onResponse(
                call: Call<MutableList<Car>>,
                response: Response<MutableList<Car>>
            ) {
                var cars: MutableList<Car>? = response.body()

                if (cars != null) {
                    setListViewData(cars)
                } else {
                    showToast("Failed to load data, try again later")
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<MutableList<Car>>, t: Throwable) {
                setLoading(false)
                showToast("Failed to load data, try again later" + t.message)
                call.cancel()
            }

        })
    }

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val view: View? = view  // property access of getView()
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_cars)

        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    //----   SET LISTVIEW DATA   ----
    private fun setListViewData(data: MutableList<Car>) {
        var adapter = CarsListAdapter(requireContext(), R.layout.car_record, data)
        val listView: ListView = requireView().findViewById(R.id.list_cars)
        listView.adapter = adapter
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}