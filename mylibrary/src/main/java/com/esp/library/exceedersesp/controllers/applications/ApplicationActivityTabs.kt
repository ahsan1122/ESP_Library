package com.esp.library.exceedersesp.controllers.applications


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.esp.library.exceedersesp.ESPApplication
import com.esp.library.utilities.common.Shared
import com.esp.library.exceedersesp.fragments.applications.UsersApplicationsFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main_applications_tabs.*
import com.esp.library.utilities.customcontrols.CustomViewPager
import com.esp.library.utilities.customcontrols.DisplayUtils
import com.esp.library.utilities.customcontrols.myBadgeView
import com.esp.library.exceedersesp.controllers.applications.AddApplicationsActivity
import java.util.*
import android.R
import android.app.Activity


class ApplicationActivityTabs : androidx.fragment.app.Fragment() {

    internal var TAG = javaClass.simpleName


    private var viewPager: CustomViewPager? = null
    internal var submit_request: UsersApplicationsFragment? = null
    internal var fm: androidx.fragment.app.FragmentManager? = null

    var open: String = ""
    var closed: String = ""
    // internal var add_account: FloatingActionButton? = null
    var count: Int = 0


    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.esp.library.R.layout.activity_main_applications_tabs, container, false)

        viewPager = v.findViewById(com.esp.library.R.id.viewpager)

        //    add_account = v.findViewById(R.id.addaccount)
        setupViewPager(viewPager!!)
        tabLayout = v.findViewById(com.esp.library.R.id.tabs)
        tabLayout?.setupWithViewPager(viewPager)
        viewPager?.setPagingEnabled(false);
        //if (ESPApplication.getInstance()?.user?.loginResponse?.role?.toLowerCase() == getString(R.string.applicantsmall)) {
            open = getString(com.esp.library.R.string.opencaps)
            closed = getString(com.esp.library.R.string.closed)
        /*} else {
            open = getString(R.string.pending)
            closed = getString(R.string.all)
        }*/

        val tab1 = LayoutInflater.from(context).inflate(com.esp.library.R.layout.custom_tab_count, null) as RelativeLayout
        val tab_text_1 = tab1.findViewById<View>(com.esp.library.R.id.tab_text) as TextView
        tab_text_1.text = open
        tabLayout?.getTabAt(0)?.setCustomView(tab1)
        val badge1 = activity?.let { myBadgeView(it, tab1.findViewById(com.esp.library.R.id.tab_badge)) }
        badge1?.updateTabBadge(0)

        val tab2 = LayoutInflater.from(context).inflate(com.esp.library.R.layout.custom_tab_count, null) as RelativeLayout
        val tab_text_2 = tab2.findViewById<View>(com.esp.library.R.id.tab_text) as TextView
        tab_text_2.text = closed
        tabLayout?.getTabAt(1)?.setCustomView(tab2)
        val badge2 = activity?.let { myBadgeView(it, tab2.findViewById(com.esp.library.R.id.tab_badge)) }
        //set the badge for the tab
        badge2?.updateTabBadge(count)




        var tabMargin = DisplayUtils.dpToPx(activity, 50)

        if (count > 0)
            tabMargin = DisplayUtils.dpToPx(activity, 40)

        reduceMarginsInTabs(tabLayout!!, tabMargin)

        addaccount?.setOnClickListener { view ->
            val manager = fragmentManager
            if (ESPApplication.getInstance().user.profileStatus == null || ESPApplication.getInstance().user.profileStatus!!.equals(getString(com.esp.library.R.string.profile_complete), ignoreCase = true)) {
                Shared.getInstance().callIntentWithResult(AddApplicationsActivity::class.java, activity, null, 2)
            } else if (ESPApplication.getInstance().user.profileStatus!!.equals(getString(com.esp.library.R.string.profile_incomplete), ignoreCase = true)) {
                Shared.getInstance().showAlertProfileMessage(getString(com.esp.library.R.string.profile_error_heading), getString(com.esp.library.R.string.profile_error_desc), activity)
            } else if (ESPApplication.getInstance().user.profileStatus!!.equals(getString(com.esp.library.R.string.profile_incomplete_admin), ignoreCase = true)) {
                Shared.getInstance().showAlertProfileMessage(getString(com.esp.library.R.string.profile_error_heading), getString(com.esp.library.R.string.profile_error_desc_admin), activity)
            }
        }




        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun reduceMarginsInTabs(tabLayout: TabLayout, marginOffset: Int) {

        val tabStrip = tabLayout.getChildAt(0)
        if (tabStrip is ViewGroup) {
            for (i in 0 until tabStrip.childCount) {
                val tabView = tabStrip.getChildAt(i)
                if (tabView.layoutParams is ViewGroup.MarginLayoutParams) {
                    (tabView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = marginOffset
                    (tabView.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = marginOffset
                }
            }

            tabLayout.requestLayout()
        }
    }

    private fun setupViewPager(viewPager: androidx.viewpager.widget.ViewPager) {
        val adapter = fragmentManager?.let { ViewPagerAdapter(it) }
        // if (ESPApplication.getInstance()?.user?.loginResponse?.role?.toLowerCase() == getString(R.string.applicantsmall)) {
        adapter?.addFragment(UsersApplicationsFragment.newInstance("open"), getString(com.esp.library.R.string.opencaps))
        adapter?.addFragment(UsersApplicationsFragment.newInstance("closed"), getString(com.esp.library.R.string.closed))
        /*} else {
            adapter?.addFragment(UsersApplicationsFragment.newInstance("pending"), getString(R.string.pending))
            adapter?.addFragment(UsersApplicationsFragment.newInstance("all"), getString(R.string.all))
        }*/
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<androidx.fragment.app.Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: androidx.fragment.app.Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    companion object {

        var tabLayout: TabLayout? = null

        fun newInstance(): ApplicationActivityTabs {

            return ApplicationActivityTabs()
        }
    }


}

