package com.example.valentines_garage.job_related_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.valentines_garage.JobFragment
import com.example.valentines_garage.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.adapters.TasksListAdapter
import com.valentines.connection.models.Job
import com.valentines.connection.models.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JobTasksFragment : Fragment() {

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get job id from args passed
        if (arguments != null) {
            job = arguments?.getParcelable(JobFragment.JOB)!!
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_tasks, container, false)

    }

    override fun onStart() {
        super.onStart()

        getTasksData()
    }

    //----   GET TASKS DATA   ----
    private fun getTasksData() {
        // show that data is loading
        setLoading(true)

        // create data to send to with request
        var jsonData: JsonObject = JsonObject()
        // TODO: change the id value to one passed to the fragment
        jsonData.addProperty("jobID", job!!.getJobID().toInt())

        Log.v("JSON: ", jsonData.toString())

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call: Call<MutableList<Task>> = apiInterface.getTasksForJob(jsonData.toString())

        call.enqueue(object : Callback<MutableList<Task>> {
            override fun onResponse(
                call: Call<MutableList<Task>>,
                response: Response<MutableList<Task>>
            ) {
                var tasks: MutableList<Task>? = response.body()

                Log.v("CALL: ", call.request().url().toString())
                Log.v("RESPONSE", response.toString())

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
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_tasks)

        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    //----   SET LISTVIEW ITEM CLICK LISTENER   ----
    private fun setListViewItemClickListener() {
//        requireView().findViewById<ListView>(R.id.list_jobs).onItemClickListener =
//            AdapterView.OnItemClickListener { adapterView, view, index, id ->
//                val ft = requireActivity().supportFragmentManager.beginTransaction()
//                ft.replace(R.id.content_frame, JobFragment())
//                ft.commit()
//            }
    }

    //----   SET LISTVIEW DATA   ----
    private fun setListViewData(data: MutableList<Task>) {

        var adapter = TasksListAdapter(requireContext(), R.layout.task_record, data)
        val listView: ListView = requireView().findViewById(R.id.list_tasks)
        listView.adapter = adapter
        Log.v("some: ", "shit")
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}