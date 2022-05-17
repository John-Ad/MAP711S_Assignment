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
import com.valentines.connection.adapters.TasksListAdapter
import com.valentines.connection.models.Employee
import com.valentines.connection.models.Task
import com.valentines.connection.models.UserOverview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserReportFragment : Fragment() {

    private var tasks: ArrayList<String> = ArrayList()
    private var username: String? = ""
    private var timeLine: Int = 0

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
        setSpinners()
    }

    private fun init() {
        getOverviewDetails()
        getTasksData()
    }

    private fun setSpinners() {
        // set timeline choice spinner
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            arrayListOf<String>("Day", "Month")
        )
        requireView().findViewById<Spinner>(R.id.spnnr_report_date).adapter = adapter

        requireView().findViewById<Spinner>(R.id.spnnr_report_date).onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    timeLine = position
                    getOverviewDetails()
                    getTasksData()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    parent!!.setSelection(0)
                }

            }

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
            if (this.timeLine == 0) apiInterface.getTaskReportForEmployeeByMonth(jsonObject.toString()) else apiInterface.getTaskReportForEmployeeByDay(
                jsonObject.toString()
            )

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

    //----   GET TASKS DATA   ----
    private fun getTasksData() {
        // show that data is loading
        setLoading(true)

        // create data to send to with request
        var jsonData: JsonObject = JsonObject()
        jsonData.addProperty("username", this.username)


        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call: Call<MutableList<Task>> =
            if (this.timeLine == 0) apiInterface.getCompleteTasksForEmployeeByDay(jsonData.toString()) else apiInterface.getCompleteTasksForEmployeeByMonth(
                jsonData.toString()
            )

        call.enqueue(object : Callback<MutableList<Task>> {
            override fun onResponse(
                call: Call<MutableList<Task>>,
                response: Response<MutableList<Task>>
            ) {
                var tasks: MutableList<Task>? = response.body()

                if (tasks != null) {
                    setListViewData(tasks)
                } else {
                    showToast(
                        "success but failure: " + response.message()
                    )
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<MutableList<Task>>, t: Throwable) {
                setLoading(false)
                Log.v("CALL: ", call.request().url().toString())
                Log.e("RESPONSE: ", t.toString())
                showToast("Failed to load data, try again later: " + t.message)
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
    private fun setListViewData(data: MutableList<Task>) {
        var adapter = TasksListAdapter(requireContext(), R.layout.task_record, data)
        val listView: ListView = requireView().findViewById(R.id.list_reports_tasks)
        listView.adapter = adapter
    }


    //----   SET OVERVIEW DATA   ----
    private fun setOverviewData(data: UserOverview) {
        requireView().findViewById<TextView>(R.id.txt_report_username).text =
            "Report for " + this.username
        requireView().findViewById<TextView>(R.id.txt_report_completed_tasks).text =
            data.completedTasks
        requireView().findViewById<TextView>(R.id.txt_report_incomplete_tasks).text =
            data.incompleteTasks
        requireView().findViewById<TextView>(R.id.txt_report_jobs_completed).text =
            data.jobsCompleted
        requireView().findViewById<TextView>(R.id.txt_report_job_most_completed).text =
            data.jobMostCompleted
        requireView().findViewById<TextView>(R.id.txt_report_job_most_incomplete).text =
            data.jobMostIncomplete
        requireView().findViewById<TextView>(R.id.txt_report_client_most_completed).text =
            data.clientMostComplete
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}