package com.example.valentines_garage

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.models.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)

        init()
    }

    //----   INIT   ----
    fun init() {

        // show that data is loading
        val prgBar: ProgressBar = findViewById<ProgressBar>(R.id.prg_jobs)
        prgBar.visibility = View.VISIBLE

        val txtView: TextView = findViewById(R.id.txt_job_temp)

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call: Call<List<Job>> = apiInterface.getAllJobs()
        call.enqueue(object : Callback<List<Job>> {
            override fun onResponse(call: Call<List<Job>>, response: Response<List<Job>>) {
                var jobs: List<Job>? = response.body()
                var jobNames: String = ""

                if (jobs != null) {
                    for (job in jobs) {
                        jobNames += job.getName() + "\n"
                    }
                } else {
                    jobNames = "You fucking suck"
                }

                txtView.text = jobNames

                prgBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Job>>, t: Throwable) {
                txtView.text = "you really fucking suck"
                prgBar.visibility = View.GONE

                call.cancel()
            }

        })

    }
}