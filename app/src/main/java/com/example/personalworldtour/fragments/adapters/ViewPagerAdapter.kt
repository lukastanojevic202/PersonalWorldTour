package com.example.personalworldtour.fragments.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    private val fragList = ArrayList<Fragment>()
    private val fragTitleList = ArrayList<String>()


    override fun getCount(): Int {
        return fragList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragTitleList[position]
    }
    fun addFragment(fragment : Fragment, title : String){
        fragList.add(fragment)
        fragTitleList.add(title)

    }

}