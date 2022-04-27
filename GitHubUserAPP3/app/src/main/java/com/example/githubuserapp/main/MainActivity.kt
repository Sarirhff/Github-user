package com.example.githubuserapp.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.detail.DetailUserActivity
import com.example.githubuserapp.favorite.FavoriteActivity
import com.example.githubuserapp.model.User
import com.example.githubuserapp.theme.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var dBinding: ActivityMainBinding

    private lateinit var viewModel: ViewModelMain
    private lateinit var adapter: UserAdapter


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(dBinding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USER, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        val pref = SettingPreference.getInstance(dataStore)
        val viewModelTheme = ViewModelProvider(this, ViewModelFactory(pref)).get(
            ViewModelTheme::class.java
        )
        viewModelTheme.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(ViewModelMain::class.java)

        dBinding.apply {
            rvSrch.layoutManager = LinearLayoutManager(this@MainActivity)
            rvSrch.setHasFixedSize(true)
            rvSrch.adapter = adapter

            buttonSrch.setOnClickListener {
                userSearch()
            }
            editQuery.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    userSearch()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.getSearchUsers().observe(this, {
            if (it != null)
                adapter.setlist(it)
            showLoadingBar(false)
        })

    }

    private fun userSearch() {
        dBinding.apply {
            val query = editQuery.text.toString()
            if (query.isEmpty()) return
            showLoadingBar(true)
            viewModel.setSearchUsers(query)
        }
    }

    private fun showLoadingBar(state: Boolean) {
        dBinding.loadingBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting->{
                startActivity(Intent(this, ThemeSetting::class.java))
            }
            R.id.menu_favorite ->{
                Intent(this, FavoriteActivity::class.java).also{
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}