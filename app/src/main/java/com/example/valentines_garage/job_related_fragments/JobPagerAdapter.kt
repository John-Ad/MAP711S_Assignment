package com.example.valentines_garage.job_related_fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.valentines_garage.R


class JobPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3;
    }

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> return JobDescriptionFragment()
            1 -> return JobTasksFragment()
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