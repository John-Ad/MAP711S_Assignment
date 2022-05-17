package com.example.valentines_garage.job_related_fragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.valentines_garage.JobFragment
import com.example.valentines_garage.JobsFragment
import com.example.valentines_garage.R
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.State
import com.valentines.connection.models.Job
import com.valentines.connection.models.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JobDescriptionFragment : Fragment() {

    var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        job = requireArguments().getParcelable(JobFragment.JOB)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_description, container, false)
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        setLoading(false)
        setData()
        if (State.getInstance().getUserType() == State.USER_ADMIN) initAdmin() else initEmployee()
    }

    private fun initEmployee() {
        requireView().findViewById<Button>(R.id.btn_job_delete).visibility = View.GONE
        requireView().findViewById<Button>(R.id.btn_job_edit).visibility = View.GONE
    }

    private fun initAdmin() {
        requireView().findViewById<Button>(R.id.btn_job_delete).setOnClickListener {
            deleteJob()
        }
    }

    private fun setData() {
        val view = requireView()
        view.findViewById<TextView>(R.id.txt_job_name).text =
            Html.fromHtml(resources.getString(R.string.job_name, job!!.getName()))
        view.findViewById<TextView>(R.id.txt_job_desc).text =
            Html.fromHtml(resources.getString(R.string.job_desc, job!!.getDescription()))
        view.findViewById<TextView>(R.id.txt_job_due).text =
            Html.fromHtml(
                resources.getString(
                    R.string.job_due,
                    job!!.getCompletionDate().subSequence(0, 10)
                )
            )
    }

    private fun deleteJob() {
        var jsonObject = JsonObject()
        jsonObject.addProperty("jobID", this.job!!.getJobID())

        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call = apiInterface.deleteJob(jsonObject)

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

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val view: View? = view  // property access of getView()
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_job_desc)

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