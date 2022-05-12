package com.example.valentines_garage.job_related_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.valentines_garage.R
import com.valentines.connection.models.Task


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
    }

    private fun setData() {
        val view = requireView()
        view.findViewById<TextView>(R.id.txt_task_name).text = task!!.getName()
        view.findViewById<TextView>(R.id.txt_task_desc).text = task!!.getDescription()
        view.findViewById<TextView>(R.id.txt_task_user).text = task!!.getUsername()
//        view.findViewById<TextView>(R.id.txt_task_completed).text =
//            if (task!!.getCompleted() == 1) "Completed" else "incomplete"
    }
}