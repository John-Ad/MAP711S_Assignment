package com.example.valentines_garage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.valentines_garage.report_fragments.UserReportFragment
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.adapters.JobsListAdapter
import com.valentines.connection.models.Employee
import com.valentines.connection.models.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReportsFragment : Fragment() {

    private var employees: ArrayList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    //----   INIT   ----
    private fun init() {
        getEmployeeData()
        setListViewItemClickListener()
    }

    //----   GET EMPLOYEE DATA   ----
    private fun getEmployeeData() {
        // show that data is loading
        setLoading(true)

        showNoRecordsAvailable(false)

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

                    if(employees.size>0) {
                        setListViewData(employees)
                    }else{
                        showNoRecordsAvailable(true)
                    }
                } else {
                    showToast("Failed to load data, try again later")
                    showNoRecordsAvailable(true)
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<MutableList<Employee>>, t: Throwable) {
                setLoading(false)
                showNoRecordsAvailable(true)
                showToast("Failed to load data, try again later")
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

    //----   SET LISTVIEW ITEM CLICK LISTENER   ----
    private fun setListViewItemClickListener() {
        requireView().findViewById<ListView>(R.id.list_reports).onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, index, id ->

                // get job details
                val string: String = (adapterView.getItemAtPosition(index) as String)

                var bundle: Bundle = Bundle()
                bundle.putString("username", string)

                val fragment = UserReportFragment()
                fragment.arguments = bundle

                val ft = requireActivity().supportFragmentManager.beginTransaction()
                ft.replace(R.id.content_frame, fragment, null)
                ft.addToBackStack(null)
                ft.commit()
            }
    }

    //----   SET LISTVIEW DATA   ----
    private fun setListViewData(data: MutableList<String>) {
        var adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, data)
        val listView: ListView = requireView().findViewById(R.id.list_reports)
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