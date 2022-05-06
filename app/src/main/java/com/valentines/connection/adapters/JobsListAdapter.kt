package com.valentines.connection.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.valentines_garage.R
import com.valentines.connection.models.Job

class JobsListAdapter(context: Context, resource: Int, objects: MutableList<Job>) :
    ArrayAdapter<Job>(context, resource, objects) {

    private val mContext = context
    private val resourceLayout = resource
    val jobs = objects

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView

        if (view == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            view = layoutInflater.inflate(resourceLayout, null)
        }

        val job: Job? = getItem(position)

        if (job != null) {
            setData(job, view!!, position)
        }

        return view!!
    }

    private fun setData(job: Job, view: View, position: Int) {
        view.findViewById<TextView>(R.id.txt_job_id).text =
            (position + 1).toString() // temp, replace with actual id later
        view.findViewById<TextView>(R.id.txt_job_name).text = job.getName()
        view.findViewById<TextView>(R.id.txt_job_due).text = job.getCompletionDate()
        view.findViewById<TextView>(R.id.txt_job_added).text = job.getDateAdded()
    }
}