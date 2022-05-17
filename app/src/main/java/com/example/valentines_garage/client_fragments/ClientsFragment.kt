package com.example.valentines_garage.client_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.valentines_garage.JobFragment
import com.example.valentines_garage.R
import com.example.valentines_garage.job_related_fragments.TaskAddFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.valentines.connection.APIClient
import com.valentines.connection.APIInterface
import com.valentines.connection.State
import com.valentines.connection.models.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientsFragment : Fragment() {
    private var clients: MutableList<Client> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clients, container, false)
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    //----   INIT   ----
    private fun init() {


        if (State.getInstance().getUserType() == State.USER_ADMIN) {
            // set fab button onClick
            requireView().findViewById<FloatingActionButton>(R.id.fab_add_client)
                .setOnClickListener {


                    val ft = requireActivity().supportFragmentManager.beginTransaction()
                    ft.replace(R.id.content_frame, ClientAddFragment(), null)
                    ft.addToBackStack(null)
                    ft.commit()
                }
        } else {
            requireView().findViewById<FloatingActionButton>(R.id.fab_add_client).visibility =
                View.GONE
        }

        getClientsData()
        setListViewItemClickListener()

    }

    //----   GET CLIENTS DATA   ----
    private fun getClientsData() {
        // show that data is loading
        setLoading(true)

        showNoRecordsAvailable(false)

        // get data from server
        val apiInterface: APIInterface = APIClient.getInstance().create(APIInterface::class.java)
        val call1: Call<MutableList<Client>> = apiInterface.getAllClients()

        call1.enqueue(object : Callback<MutableList<Client>> {
            override fun onResponse(
                call: Call<MutableList<Client>>,
                response: Response<MutableList<Client>>
            ) {
                var cls: MutableList<Client>? = response.body()

                if (cls != null) {

                    clients = cls

                    if (cls!!.size > 0) {
                        setListViewData(cls)
                    } else {
                        showNoRecordsAvailable(true)
                    }
                } else {
                    showToast("Failed to load data, try again later: " + response.body())
                    showNoRecordsAvailable(true)
                }

                setLoading(false)
            }

            override fun onFailure(call: Call<MutableList<Client>>, t: Throwable) {
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
        val prgBar: ProgressBar = view!!.findViewById<ProgressBar>(R.id.prg_clients)

        prgBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    //----   SET LISTVIEW ITEM CLICK LISTENER   ----
    private fun setListViewItemClickListener() {
        requireView().findViewById<ListView>(R.id.list_clients).onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, index, id ->

                var bundle: Bundle = Bundle()
                bundle.putParcelable("client", clients[index])

                val fragment = ClientViewFragment()
                fragment.arguments = bundle

                val ft = requireActivity().supportFragmentManager.beginTransaction()
                ft.replace(R.id.content_frame, fragment, null)
                ft.addToBackStack(null)
                ft.commit()
            }
    }

    //----   SET LISTVIEW DATA   ----
    private fun setListViewData(data: MutableList<Client>) {
        var dataToSet: ArrayList<String> = data.mapTo(arrayListOf()) { it.name }

        var adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, dataToSet)
        val listView: ListView = requireView().findViewById(R.id.list_clients)
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