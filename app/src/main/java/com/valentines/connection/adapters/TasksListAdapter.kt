package com.valentines.connection.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.valentines_garage.R
import com.valentines.connection.models.Task

class TasksListAdapter(context: Context, resource: Int, objects: MutableList<Task>) :
    ArrayAdapter<Task>(context, resource, objects) {

    private val mContext = context
    private val resourceLayout = resource
    val tasks = objects

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView

        if (view == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            view = layoutInflater.inflate(resourceLayout, null)
        }

        val task: Task? = getItem(position)

        if (task != null) {
            setData(task, view!!, position)
        }

        return view!!
    }

    private fun setData(task: Task, view: View, position: Int) {
        view.findViewById<TextView>(R.id.txt_task_name).text = task.getName()
        view.findViewById<TextView>(R.id.txt_task_user).text = task.getUsername()
        view.findViewById<TextView>(R.id.txt_task_completed).text =
            if (task.isCompleted()) "yes" else "no"
    }
}