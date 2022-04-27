package com.example.githubuserapp.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.ActivityDetailUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityDetailUserBinding
    private lateinit var viewModel: ViewModelDetailUser

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Detail User"

        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val username = intent.getStringExtra(EXTRA_USER)
        val id = intent.getIntExtra(EXTRA_ID,0 )
        val urlAvatar = intent.getStringExtra(EXTRA_AVATAR)
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        showLoading(true)
        viewModel = ViewModelProvider(this).get(ViewModelDetailUser::class.java)
        username?.let { viewModel.setUserDetail(it) }
        viewModel.getUserDetail().observe(this, {
            if (it !== null) {
                showLoading(false)
                _binding.apply {
                    name.text = it.name
                    userName.text = it.login
                    gitFollowers.text = getString(R.string.follower, it.followers)
                    gitFollowing.text = getString(R.string.following, it.following)
                    gitRepository.text = getString(R.string.repository, it.public_repos)
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatar_url)
                        .into(photo)
                    Location.text = it.location
                    Company.text = it.company
                }
            }
        })
        var isCheck = false
        CoroutineScope(Dispatchers.IO).launch{
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if(count!= null){
                    if(count>0){
                        _binding.btntoglleFav.isChecked = true
                        isCheck = true
                    } else{
                        _binding.btntoglleFav.isChecked = false
                        isCheck = false
                    }
                }
            }
        }
        _binding.btntoglleFav.setOnClickListener{
            isCheck = !isCheck
            if(isCheck){
                viewModel.addToFavorite(username!!, id, urlAvatar!!)
            }else{
                viewModel.removeFromFavorite(id)
            }
            _binding.btntoglleFav.isChecked = isCheck
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        _binding.apply {
            viewPager.adapter = sectionPagerAdapter
            barTabs.setupWithViewPager(viewPager)
        }
    }
    private fun showLoading(state: Boolean) {
        _binding.progresBar.visibility = if (state) View.VISIBLE else View.GONE
    }
    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR ="extra_avatarurl"
    }
}