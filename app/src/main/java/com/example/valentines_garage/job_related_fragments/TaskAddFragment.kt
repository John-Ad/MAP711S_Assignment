package com.example.valentines_garage.job_related_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.valentines_garage.JobFragment
import com.example.valentines_garage.R
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.models.Employee
import com.valentines.connection.models.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class TaskAddFragment : Fragment() {

    private var employees: ArrayList<String> = ArrayList()
    private var jobID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get job id
        jobID = requireArguments().getInt(JobFragment.JOB)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_add, container, false)
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        getData()
        initButtons()
    }

    //----   INIT BUTTONS   ----
    private fun initButtons() {
        // SAVE BUTTON
        requireView().findViewById<Button>(R.id.btn_task_save).setOnClickListener {
            getUserInput()
        }
        // BACK BUTTON
        requireView().findViewById<Button>(R.id.btn_task_back).setOnClickListener {
            goBack()
        }
    }

    //----   GO TO PREV FRAG   ----
    private fun goBack() {
        requireActivity().onBackPressed()
    }

    //----   GET DATA   ----
    private fun getData() {
        // show that data is loading
        setLoading(true)

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call1: Call<MutableList<Employee>> = apiInterface.getEmployeeNames()
        call1.enqueue(object : Callback<MutableList<Employee>> {
            override fun onResponse(
                call: Call<MutableList<Employee>>,
                response: Response<MutableList<Employee>>
            ) {
                var emps: MutableList<Employee>? = response.body()

                if (emps != null) {

                    employees.clear()
                    for (e in emps) {
                        employees.add(e.username)
                    }

                    setEmployeeSpinner()
                } else {
                    showToast("Failed to load data, try again later")
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<MutableList<Employee>>, t: Throwable) {
                setLoading(false)
                showToast("Failed to load data, try again later")
                call.cancel()
            }

        })
    }

    //----   GET USER INPUT   ----
    private fun getUserInput() {
        val view = requireView()

        val name = view.findViewById<EditText>(R.id.edt_task_name).text.toString()
        val desc = view.findViewById<EditText>(R.id.edt_task_desc).text.toString()
        val empIndex = view.findViewById<Spinner>(R.id.spnr_emp).selectedItemPosition
        val empUsername = employees[empIndex]


        when {
            name.isEmpty() -> {
                showToast("Enter a name for the task"); return
            }
            desc.isEmpty() -> {
                showToast("Enter a description for the task"); return
            }
        }

        // convert to json
        var jsonData: JsonObject = JsonObject()
        jsonData.addProperty("Job_ID", this.jobID)
        jsonData.addProperty("Name", name)
        jsonData.addProperty("Description", desc)
        jsonData.addProperty("Username", empUsername)

        // send request
        addTask(jsonData)
    }

    //----   CLEAR INPUT   ----
    private fun clearInput() {
        requireView().findViewById<EditText>(R.id.edt_task_name).setText("")
        requireView().findViewById<EditText>(R.id.edt_task_desc).setText("")
    }

    //----   ADD TASK   ----
    private fun addTask(data: JsonObject) {
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call = apiInterface.addTask(data)

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
                    showToast("Failed to add job, try again later" + response.body().toString())
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                setLoading(false)
                showToast("Failed to add job, try again later" + t.message)
                call.cancel()
            }
        })
    }

    //----   SET EMPLOYEE SPINNER DATA   ----
    private fun setEmployeeSpinner() {
        var adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, employees)

        requireView().findViewById<Spinner>(R.id.spnr_emp).adapter = adapter
    }

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val view: View? = view  // property access of getView()
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_addTask)
        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}