package com.example.valentines_garage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.valentines_garage.job_related_fragments.JobPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.valentines.connection.models.Job

class JobFragment : Fragment() {

    private var job: Job? = null

    companion object {
        const val JOB = "job"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.v("FRAG CREATED", "Job Fragment")

        // get job from bundle
        job = requireArguments().getParcelable(JOB)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job, container, false)
    }

    override fun onStart() {
        super.onStart()

        Log.v("FRAG STARTED", "Job Fragment")

        // set page adapter
        val adapter: JobPagerAdapter =
            JobPagerAdapter(childFragmentManager, this.job!!) // NB!! use childFragmentManager
        val pager = requireView().findViewById<ViewPager>(R.id.vpgr_job)
        pager.adapter = adapter

        // setup view pager with tab layout
        requireView().findViewById<TabLayout>(R.id.tab_job).setupWithViewPager(pager)
    }


}