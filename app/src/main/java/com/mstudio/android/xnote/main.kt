package com.mstudio.android.xnote

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mstudio.android.xnote.activity.add_activity
import com.mstudio.android.xnote.adapter.adapter
import com.mstudio.android.xnote.database.DatabaseHelper
import com.mstudio.android.xnote.model.model


class main : AppCompatActivity() {
    lateinit var fab: FloatingActionButton

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: adapter
    var isrunfrist: Boolean = false
    val handler = Handler()
    val dataLiveData = MutableLiveData<List<String>>() // แทน YourDataType ด้วยชนิดข้อมูลที่คุณใช้

    val REQUEST_CODE = 1
    private lateinit var appbartoggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var appBar: AppBarLayout
    private lateinit var toolbar: Toolbar

    private var actionMode: ActionMode? = null
    private lateinit var swiprefresh : SwipeRefreshLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        drawer = findViewById(R.id.drawerLayout)
        appBar = findViewById(R.id.appBarLayout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        appbartoggle =
            ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recycleview_main)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(this)
        drawer.addDrawerListener(appbartoggle)
        appbartoggle.syncState()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        adapter = adapter(this, getDataFromDatabase())
        recyclerView.adapter = adapter
        val itemAnimator: ItemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 1000
        itemAnimator.removeDuration = 1000
        recyclerView.itemAnimator = itemAnimator
        fab = findViewById(R.id.go_add_activity)


        fab.setOnClickListener {
            val intent = Intent(this, add_activity::class.java)
            startActivity(intent)
        }
        swiprefresh = findViewById(R.id.swiperefresh)

        swiprefresh.setOnRefreshListener {

            swiprefresh.isRefreshing = true
            adapter.updateDatadefault(getDataFromDatabase())
            swiprefresh.isRefreshing = false

        }
        handler.postDelayed({
            isrunfrist = true

        }, 500)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar, menu) // เรียกใช้เมนูที่คุณสร้าง


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (appbartoggle.onOptionsItemSelected(item))
            when (item.itemId) {
                R.id.homeFragment -> {

                    return true
                }
                else -> return super.onOptionsItemSelected(item)
            }
        return true
    }

    private fun getDataFromDatabase(): List<model> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM my_table", null)

        val data = ArrayList<model>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val title = cursor.getString(cursor.getColumnIndex("title"))?: ""
            val subtitle = cursor.getString(cursor.getColumnIndex("subtitle"))?: ""

            val color = cursor.getString(cursor.getColumnIndex("color"))?: ""
            val date = cursor.getString(cursor.getColumnIndex("date"))?: ""

            val item = model(id, title, subtitle, color, date)
            data.add(item)


        }
        cursor.close()
        db.close()
        data.reverse()

        return data
    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = resources.getColor(android.R.color.transparent, null)

    }

    fun updatedata() {

        handler.postDelayed({
            val layoutManager = recyclerView.layoutManager
            if (layoutManager is LinearLayoutManager) {
                val currentPosition = layoutManager.findFirstVisibleItemPosition()
                if(currentPosition>1){


                }else{
                    adapter.updateData(getDataFromDatabase())
                    recyclerView.smoothScrollToPosition(0)
                }
            }
        }, 500)

    }
}