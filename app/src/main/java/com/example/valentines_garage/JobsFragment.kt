package com.example.valentines_garage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.valentines_garage.job_related_fragments.JobAddFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.State
import com.valentines.connection.adapters.JobsListAdapter
import com.valentines.connection.models.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.v("FRAG CREATED", "Jobs Fragment")

        return inflater.inflate(R.layout.fragment_jobs, container, false)
    }

    override fun onStart() {
        super.onStart()
        init()
        if (State.getInstance().getUserType() == State.USER_ADMIN) initAdmin() else initEmployee()
    }

    private fun init() {
        setListViewItemClickListener()
    }

    //----   INIT ADMIN   ----
    private fun initAdmin() {
        getJobsDataAdmin()
        setButtonListeners()
    }

    //----   INIT EMPLOYEE   ----
    private fun initEmployee() {

        // hide FAB
        requireView().findViewById<FloatingActionButton>(R.id.fab_add_job).visibility = View.GONE

        // get jobs for employee
        getJobsDataEmployee()
    }

    //----   SET BUTTON LISTENERS   ----
    private fun setButtonListeners() {

        // set FAB listener
        val view: View = requireView()
        view.findViewById<FloatingActionButton>(R.id.fab_add_job).setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_frame, JobAddFragment(), null)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    //----   GET JOBS DATA ADMIN   ----
    private fun getJobsDataAdmin() {
        // show that data is loading
        setLoading(true)

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call: Call<MutableList<Job>> = apiInterface.getAllJobs()
        call.enqueue(object : Callback<MutableList<Job>> {
            override fun onResponse(
                call: Call<MutableList<Job>>,
                response: Response<MutableList<Job>>
            ) {
                var jobs: MutableList<Job>? = response.body()

                if (jobs != null) {
                    setListViewData(jobs)
                } else {
                    showToast("Failed to load data, try again later")
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<MutableList<Job>>, t: Throwable) {
                setLoading(false)
                showToast("Failed to load data, try again later")
                call.cancel()
            }

        })
    }

    //----   GET JOBS DATA EMPLOYEE   ----
    private fun getJobsDataEmployee() {
        // show that data is loading
        setLoading(true)

        // setup json data
        var jsonObject = JsonObject()
        jsonObject.addProperty("username", State.getInstance().getUsername())

        Log.v("Data-Request", jsonObject.toString())

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call: Call<MutableList<Job>> = apiInterface.getAllJobsForEmployee(jsonObject.toString())
        call.enqueue(object : Callback<MutableList<Job>> {
            override fun onResponse(
                call: Call<MutableList<Job>>,
                response: Response<MutableList<Job>>
            ) {
                var jobs: MutableList<Job>? = response.body()

                if (jobs != null) {
                    setListViewData(jobs)
                } else {
                    showToast("Failed to load data, try again later")
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<MutableList<Job>>, t: Throwable) {
                setLoading(false)
                showToast("Failed to load data, try again later")
                call.cancel()
            }

        })
    }

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val view: View? = view  // property access of getView()
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_jobs)

        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    //----   SET LISTVIEW ITEM CLICK LISTENER   ----
    private fun setListViewItemClickListener() {
        requireView().findViewById<ListView>(R.id.list_jobs).onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, index, id ->

                // get job details
                val job: Job = (adapterView.getItemAtPosition(index) as Job)

                var bundle: Bundle = Bundle()
                bundle.putParcelable(JobFragment.JOB, job)

                val jobFragment: JobFragment = JobFragment()
                jobFragment.arguments = bundle

                val ft = requireActivity().supportFragmentManager.beginTransaction()
                ft.replace(R.id.content_frame, jobFragment, job.getJobID())
                ft.addToBackStack(job.getJobID())
                ft.commit()
            }
    }

    //----   SET LISTVIEW DATA   ----
    private fun setListViewData(data: MutableList<Job>) {
        var adapter = JobsListAdapter(requireContext(), R.layout.job_record, data)
        val listView: ListView = requireView().findViewById(R.id.list_jobs)
        listView.adapter = adapter
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}