package com.example.valentines_garage.report_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.valentines_garage.R
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.models.Employee
import com.valentines.connection.models.UserOverview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserReportFragment : Fragment() {

    private var tasks: ArrayList<String> = ArrayList()
    private var username: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get args
        username = requireArguments().getString("username")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_report, container, false)
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        getOverviewDetails()
    }

    //----   GET OVERVIEW DETAILS   ----
    private fun getOverviewDetails() {
        // show that data is loading
        setLoading(true)

        // data
        var jsonObject = JsonObject()
        jsonObject.addProperty("username", this.username)

        Log.v("Request-data", jsonObject.toString())

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call1: Call<UserOverview> =
            apiInterface.getTaskReportForEmployeeByMonth(jsonObject.toString())

        call1.enqueue(object : Callback<UserOverview> {
            override fun onResponse(
                call: Call<UserOverview>,
                response: Response<UserOverview>
            ) {
                var data: UserOverview? = response.body()

                if (data != null) {

                    setOverviewData(data)
                } else {
                    showToast("Failed to load data, try again later")
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<UserOverview>, t: Throwable) {
                setLoading(false)
                showToast("Failed to load data, try again later" + t.message)
                call.cancel()
            }

        })
    }

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val view: View? = view  // property access of getView()
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_reports)

        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    //----   SET LISTVIEW DATA   ----
    private fun setListViewData(data: MutableList<String>) {
        var adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, data)
        val listView: ListView = requireView().findViewById(R.id.list_reports)
        listView.adapter = adapter
    }


    //----   SET OVERVIEW DATA   ----
    private fun setOverviewData(data: UserOverview) {
        requireView().findViewById<TextView>(R.id.txt_report_completed_tasks).text =
            "Completed Tasks: " + data.completedTasks
        requireView().findViewById<TextView>(R.id.txt_report_incomplete_tasks).text =
            "Incomplete Tasks: " + data.incompleteTasks
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}