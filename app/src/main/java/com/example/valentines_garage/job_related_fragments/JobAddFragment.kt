package com.example.valentines_garage.job_related_fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.valentines_garage.R
import com.google.gson.JsonObject
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.models.ClientDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer


class JobAddFragment : Fragment() {

    private var responseCount: Int = 0
    private var vins: ArrayList<String> = ArrayList()
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_add, container, false)
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        getData()
        setCalendar()
        initButtons()
    }

    //----   INIT BUTTONS   ----
    private fun initButtons() {
        // SAVE BUTTON
        requireView().findViewById<Button>(R.id.btn_job_save).setOnClickListener {
            getUserInput()
        }
        // BACK BUTTON
        requireView().findViewById<Button>(R.id.btn_job_back).setOnClickListener {
            goBack()
        }
    }

    //----   GO TO PREV FRAG   ----
    private fun goBack() {
        requireActivity().onBackPressed()
    }

    //----   SET CALENDAR   ----
    private fun setCalendar() {
        val date =
            OnDateSetListener { view, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                setDateText()
            }

        requireView().findViewById<EditText>(R.id.edt_job_completion_date).setOnClickListener {
            DatePickerDialog(
                requireContext(),
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    //----   SET DATE TEXT   ----
    private fun setDateText() {
        val format = "yy-MM-dd"
        val dateFormatter = SimpleDateFormat(format)
        requireView().findViewById<EditText>(R.id.edt_job_completion_date)
            .setText(dateFormatter.format(calendar.time))
    }

    //----   GET DATA   ----
    private fun getData() {
        // show that data is loading
        setLoading(true)

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call1: Call<MutableList<ClientDetails>> = apiInterface.getClientDetails()
        call1.enqueue(object : Callback<MutableList<ClientDetails>> {
            override fun onResponse(
                call: Call<MutableList<ClientDetails>>,
                response: Response<MutableList<ClientDetails>>
            ) {
                var details: MutableList<ClientDetails>? = response.body()

                if (details != null) {

                    vins.clear()
                    for (d in details) {
                        vins.add(d.vin)
                    }

                    setVinSpinner(details)
                } else {
                    showToast("Failed to load data, try again later")
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<MutableList<ClientDetails>>, t: Throwable) {
                setLoading(false)
                showToast("Failed to load data, try again later")
                call.cancel()
            }

        })
    }

    //----   GET USER INPUT   ----
    private fun getUserInput() {
        val view = requireView()

        val name = view.findViewById<EditText>(R.id.edt_job_name).text.toString()
        val desc = view.findViewById<EditText>(R.id.edt_job_desc).text.toString()
        val date = view.findViewById<EditText>(R.id.edt_job_completion_date).text.toString()
        val vinIndex = view.findViewById<Spinner>(R.id.spnr_vin).selectedItemPosition
        val vin = vins[vinIndex]

        Log.v("FUCK", "YOU SUCK")

        when {
            name.isEmpty() -> {
                showToast("Enter a name for the job"); return
            }
            desc.isEmpty() -> {
                showToast("Enter a description for the job"); return
            }
            date.isEmpty() -> {
                showToast("Enter a date for the job"); return
            }
            vin.isEmpty() -> {
                showToast("Choose a client"); return
            }
        }

        // convert to json
        var jsonData: JsonObject = JsonObject()
        jsonData.addProperty("VIN", vin)
        jsonData.addProperty("Name", name)
        jsonData.addProperty("Description", desc)
        jsonData.addProperty("Completion_Date", date)

        // send request
        addJob(jsonData)
    }

    //----   ADD JOB   ----
    private fun addJob(data: JsonObject) {
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call = apiInterface.addJob(data)

        Log.v("FUCK", "SO ASS")

        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                var res: String? = response.body()

                if (res != null) {
                    showToast(res)
                } else {
                    showToast("Failed to add job, try again later"+response.body().toString())
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                setLoading(false)
                showToast("Failed to add job, try again later" + t.message)
                call.cancel()
            }
        })
    }

    //----   SET VIN SPINNER DATA   ----
    private fun setVinSpinner(clients: MutableList<ClientDetails>) {
        var cls = ArrayList<String>()
        for (c in clients) {
            cls.add(c.clientName + ": " + c.name)
        }

        var adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, cls)

        requireView().findViewById<Spinner>(R.id.spnr_vin).adapter = adapter
    }

    //----   SET LOADING   ----
    private fun setLoading(loading: Boolean) {
        val view: View? = view  // property access of getView()
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_addJobs)
        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
//        if (!loading) {
//            if (responseCount >= 2) {
//                prgBar.visibility = View.GONE
//                responseCount = 0
//            } else {
//                responseCount++
//            }
//        } else {
//            prgBar.visibility = View.VISIBLE
//        }
    }

    //----   SHOW TOAST   ----
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}