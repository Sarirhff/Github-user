package com.example.githubuserapp.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.FragmentBinding
import com.example.githubuserapp.detail.DetailUserActivity
import com.example.githubuserapp.detail.ViewModelFollowing
import com.example.githubuserapp.main.UserAdapter

class FragmentFollowing  : Fragment(R.layout.fragment) {

    private var dbinding: FragmentBinding? = null
    private val binding get() = dbinding!!

    private lateinit var viewModel: ViewModelFollowing
    private lateinit var adapter: UserAdapter
    private lateinit var usernm: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbinding = FragmentBinding.bind(view)

        val Arg = arguments
        usernm = Arg?.getString(DetailUserActivity.EXTRA_USER).toString()

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        dbinding.apply {
            this!!.rvfollow.setHasFixedSize(true)
            rvfollow.layoutManager = LinearLayoutManager(activity)
            rvfollow.adapter = adapter
        }
        showloading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(ViewModelFollowing::class.java)
        viewModel.setlistfollow(usernm)
        viewModel.getlistfollowing().observe(viewLifecycleOwner) {
            if (it !== null) {
                adapter.setlist(it)
                showloading(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbinding = null

    }

    private fun showloading(state: Boolean) {
        if (state) {
            dbinding!!.progresBar.visibility = View.VISIBLE
        } else {
            dbinding!!.progresBar.visibility = View.GONE
        }
    }
}