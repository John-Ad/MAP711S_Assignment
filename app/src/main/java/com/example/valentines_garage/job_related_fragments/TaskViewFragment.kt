package com.example.valentines_garage.job_related_fragments

import android.os.Bundle
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.valentines_garage.R
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.State
import com.valentines.connection.models.PostResponse
import com.valentines.connection.models.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaskViewFragment : Fragment() {
    private var task: Task? = null

    companion object {
        const val TASK = "task"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get task from bundle
        task = requireArguments().getParcelable(TASK)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_view, container, false)
    }

    override fun onStart() {
        super.onStart()

        setData()
        init()
    }

    private fun init() {
        setLoading(false)

        // set back button listener
        requireView().findViewById<Button>(R.id.btn_task_back).setOnClickListener {
            goBack()
        }

        if (State.getInstance().getUserType() == State.USER_ADMIN) {
            initAdmin()
        } else {
            initEmployee()
        }
    }

    private fun initAdmin() {
        // hide if user is admin
        requireView().findViewById<Button>(R.id.btn_task_edit_mark_as_done).visibility = View.GONE

        // make non editable
        requireView().findViewById<EditText>(R.id.edt_task_comments).inputType = InputType.TYPE_NULL


        requireView().findViewById<Button>(R.id.btn_task_delete).setOnClickListener {
            deleteTask()
        }
    }

    private fun initEmployee() {

        requireView().findViewById<Button>(R.id.btn_task_edit).visibility = View.GONE
        requireView().findViewById<Button>(R.id.btn_task_delete).visibility = View.GONE


        // hide if employee is not assigned task
        if (!State.getInstance().getUsername().equals(this.task!!.getUsername())) {
            // make non editable
            requireView().findViewById<EditText>(R.id.edt_task_comments).inputType =
                InputType.TYPE_NULL

            // hide
            requireView().findViewById<Button>(R.id.btn_task_edit_mark_as_done).visibility =
                View.GONE
        } else {

            // allow user to mark as complete or incomplete
            setMarkAsCompleteButtonText()
            requireView().findViewById<Button>(R.id.btn_task_edit_mark_as_done).setOnClickListener {
                markAsCompleteOrIncomplete()
            }

        }
    }

    private fun setMarkAsCompleteButtonText() {

        // make non editable if complete
        if (this.task!!.isCompleted()) {
            requireView().findViewById<EditText>(R.id.edt_task_comments).inputType =
                InputType.TYPE_NULL
        } else {


            Log.v(
                "ahhg",
                requireView().findViewById<EditText>(R.id.edt_task_comments).lineCount.toString()
            )
        }



        requireView().findViewById<Button>(R.id.btn_task_edit_mark_as_done).text =
            if (this.task!!.isCompleted()) "Mark as Incomplete" else "Mark as Complete"
    }

    private fun markAsCompleteOrIncomplete() {
        // show that data is loading
        setLoading(true)

        // create data to send to with request
        var jsonData: JsonObject = JsonObject()
        jsonData.addProperty("taskID", this.task!!.getTaskID())

        // add comments if marking as complete
        if (!this.task!!.isCompleted()) {
            jsonData.addProperty("comments", getComments())
        }

        Log.v("JSON: ", jsonData.toString())

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call: Call<PostResponse> =
            if (this.task!!.isCompleted()) apiInterface.markTaskAsIncomplete(jsonData) else apiInterface.markTaskAsComplete(
                jsonData
            )

        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {
                var res: PostResponse? = response.body()

                // change status of task
                task!!.setCompleted(if (task!!.isCompleted()) 0 else 1)
                setMarkAsCompleteButtonText()

                if (res != null) {
                    showToast(res.getStatus())
                } else {
                    showToast(
                        "Failed to complete request, try again later" + response.body().toString()
                    )
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                setLoading(false)
                showToast("Failed to load data, try again later: " + t.message)
                call.cancel()
            }

        })
    }

    private fun setData() {
        val view = requireView()
        view.findViewById<TextView>(R.id.txt_task_name).text = task!!.getName()
        view.findViewById<TextView>(R.id.txt_task_desc).text = task!!.getDescription()
        view.findViewById<TextView>(R.id.txt_task_user).text = task!!.getUsername()
        view.findViewById<TextView>(R.id.edt_task_comments).text = task!!.getComments()
    }

    private fun deleteTask() {
        var jsonObject = JsonObject()
        jsonObject.addProperty("taskID", this.task!!.getTaskID())

        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call = apiInterface.deleteTask(jsonObject)

        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {
                var res: PostResponse? = response.body()

                if (res != null) {
                    showToast(res.getStatus())
                    goBack()
                } else {
                    showToast("Failed to delete task: " + response.body().toString())
                    setLoading(false)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                setLoading(false)
                showToast("Failed to add job, try again later" + t.message)
                call.cancel()
            }
        })
    }

    //----   GET COMMENTS   ----
    private fun getComments(): String {
        val data = requireView().findViewById<EditText>(R.id.edt_task_comments).text.toString()
        if (data.isEmpty()) {
            return ""
        }
        return data
    }

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val view: View? = view  // property access of getView()
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_mark_task)

        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun goBack() {
        requireActivity().onBackPressed()
    }
}