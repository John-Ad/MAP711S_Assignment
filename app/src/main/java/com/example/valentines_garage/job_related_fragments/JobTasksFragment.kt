package com.example.valentines_garage.job_related_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.example.valentines_garage.JobFragment
import com.example.valentines_garage.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.State
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

        Log.v("FRAG CREATED", "Job Tasks Fragment")

        // get job id from args passed
        if (arguments != null) {
            job = arguments?.getParcelable(JobFragment.JOB)!!
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_tasks, container, false)

    }

    override fun onStart() {
        super.onStart()

        initSpinners()
        setListViewItemClickListener()
        getTasksData(false)

        if (State.getInstance().getUserType() == State.USER_ADMIN) {
            initButtons()
        } else {
            initEmployee()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.v("FRAG DESTROYED", "Job Tasks Fragment")

    }

    private fun initButtons() {
        // set FAB listener
        val view: View = requireView()
        view.findViewById<FloatingActionButton>(R.id.fab_add_task).setOnClickListener {
            var bundle = Bundle()
            bundle.putInt(JobFragment.JOB, job!!.getJobID().toInt())
            val taskAddFragment = TaskAddFragment()
            taskAddFragment.arguments = bundle

            val ft = requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_frame, taskAddFragment, null)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    private fun initSpinners() {


        // init task view spinner
        val taskViewOpts: ArrayList<String> = arrayListOf("Incomplete", "Completed")
        var taskViewAdapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                taskViewOpts
            )
        val taskViewSpinner = requireView().findViewById<Spinner>(R.id.spnr_task_view)
        taskViewSpinner.adapter = taskViewAdapter
        taskViewSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getTasksData(position == 1)  // position 1 means completed
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                parent!!.setSelection(0)
            }

        }

    }

    //----   INIT EMPLOYEE   ----
    private fun initEmployee() {
        requireView().findViewById<FloatingActionButton>(R.id.fab_add_task).visibility = View.GONE
    }

    //----   GET TASKS DATA   ----
    private fun getTasksData(completed: Boolean) {
        // show that data is loading
        setLoading(true)

        showNoRecordsAvailable(false)

        // create data to send to with request
        var jsonData: JsonObject = JsonObject()
        // TODO: change the id value to one passed to the fragment
        jsonData.addProperty("jobID", job!!.getJobID().toInt())

        Log.v("JSON: ", jsonData.toString())

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call: Call<MutableList<Task>> =
            if (!completed) apiInterface.getTasksForJobIncomplete(jsonData.toString()) else apiInterface.getTasksForJobComplete(
                jsonData.toString()
            )

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
                    if (tasks.size == 0) {
                        showNoRecordsAvailable(true)
                    }
                } else {
                    showToast(
                        "success but failure: " + response.message()
                    )
                    showNoRecordsAvailable(true)
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<MutableList<Task>>, t: Throwable) {
                setLoading(false)
                showNoRecordsAvailable(true)
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
        requireView().findViewById<ListView>(R.id.list_tasks).onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, index, id ->

                // get task details
                val task: Task = (adapterView.getItemAtPosition(index) as Task)

                var bundle: Bundle = Bundle()
                bundle.putParcelable(TaskViewFragment.TASK, task)

                val taskViewFragment = TaskViewFragment()
                taskViewFragment.arguments = bundle

                val ft = requireActivity().supportFragmentManager.beginTransaction()
                ft.replace(R.id.content_frame, taskViewFragment, task.getTaskID().toString())
                ft.addToBackStack(task.getTaskID().toString())
                ft.commit()
            }
    }

    //----   SET LISTVIEW DATA   ----
    private fun setListViewData(data: MutableList<Task>) {

        var adapter = TasksListAdapter(requireContext(), R.layout.task_record, data)
        val listView: ListView = requireView().findViewById(R.id.list_tasks)
        listView.adapter = adapter
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    //----   SHOW NO RECORDS AVAILABLE   ----
    private fun showNoRecordsAvailable(show: Boolean) {
        requireView().findViewById<TextView>(R.id.txt_no_items).visibility =
            if (show) View.VISIBLE else View.GONE
    }
}