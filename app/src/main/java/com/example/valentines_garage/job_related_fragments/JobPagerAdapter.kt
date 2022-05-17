package com.example.valentines_garage.job_related_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.valentines_garage.JobFragment
import com.valentines.connection.models.Job


class JobPagerAdapter(fm: FragmentManager, job: Job) : FragmentPagerAdapter(fm) {
    val job: Job = job

    override fun getCount(): Int {
        return 3;
    }

    override fun getItem(position: Int): Fragment {

        val bundle = Bundle()
        bundle.putParcelable(JobFragment.JOB, job)

        when (position) {
            0 -> {
                val jobDescriptionFragment = JobDescriptionFragment()
                jobDescriptionFragment.arguments = bundle

                return jobDescriptionFragment
            }
            1 -> {
                val jobTasksFragment = JobTasksFragment()
                jobTasksFragment.arguments = bundle

                return jobTasksFragment
            }
            2 -> return JobUsersFragment()
        }

        return JobDescriptionFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Overview"
            1 -> return "Tasks"
            2 -> return "Users"
        }

        return null
    }
}