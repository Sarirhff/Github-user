package com.example.githubuserapp.detail

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubuserapp.R
import com.example.githubuserapp.fragment.FragmentFollowers
import com.example.githubuserapp.fragment.FragmentFollowing

class SectionPagerAdapter(private val contx: Context, fm: FragmentManager, data:Bundle):FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentBundle : Bundle

    init {
        fragmentBundle = data
    }
    @StringRes

    private val tab_title = intArrayOf(R.string.follower, R.string.following)
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment?= null
        when (position){

            0 -> fragment = FragmentFollowers()
            1 -> fragment = FragmentFollowing()
        }
        fragment?.arguments = this.fragmentBundle
        return  fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return contx.resources.getString(tab_title[position])
    }

}