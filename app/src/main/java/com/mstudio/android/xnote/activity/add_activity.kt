package com.mstudio.android.xnote.activity

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.mstudio.android.xnote.R
import com.mstudio.android.xnote.database.DatabaseHelper
import com.mstudio.android.xnote.fragment.Fragment_backgroundcolor
import com.mstudio.android.xnote.fragment.Fragment_color
import java.util.*


class add_activity : AppCompatActivity() {
    lateinit var ed_title : EditText
    lateinit var ed_subtitle : EditText
    lateinit var date_texview : TextView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var toolbar : Toolbar
    private lateinit var appBar : AppBarLayout

    val calendar = Calendar.getInstance()
    lateinit var btncolor : CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_activity)

        val db = openOrCreateDatabase("my_database.db", Context.MODE_PRIVATE, null)

        toolbar = findViewById(R.id.toolbar)
        appBar = findViewById(R.id.appBarLayout)

        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        dbHelper = DatabaseHelper(this)
        ed_title = findViewById(R.id.ed_title)
        ed_subtitle = findViewById(R.id.ed_subtitle)
        date_texview = findViewById(R.id.dateTextView)
        btncolor = findViewById(R.id.btn_color)




        btncolor.setOnClickListener{
            showbottom_color()
        }
        var keynote  = intent.getStringExtra("keynote")
        var keynote_id  = intent.getStringExtra("keynote_id")


        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // มีการเริ่มจาก 0 จึงต้องบวก 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val itemId = keynote_id
        date_texview.setText(day.toString()+"/"+month.toString()+"/"+year.toString()+" "+hour.toString()+":"+minute.toString())

        val query = "SELECT * FROM my_table WHERE id = $itemId"

        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id: Int = cursor.getInt(cursor.getColumnIndex("id"))
            val title: String = cursor.getString(cursor.getColumnIndex("title"))
            val subtitle: String = cursor.getString(cursor.getColumnIndex("subtitle"))
            val color: String = cursor.getString(cursor.getColumnIndex("color"))
            val date: String = cursor.getString(cursor.getColumnIndex("date"))

            if (keynote.equals("1")) {
                getSupportActionBar()?.setTitle(R.string.title_edit_activity)
                ed_title.setText(title)
                ed_subtitle.setText(subtitle)
                date_texview.setText(date.toString())
            }
        }
        cursor.close()
        db.close()


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
             finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onPause() {
        super.onPause()
        val title: String = ed_title.text.toString()
        val subtitle: String = ed_subtitle.text.toString()



        var keynote  = intent.getStringExtra("keynote")
        var keynote_id  = intent.getStringExtra("keynote_id")

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // มีการเริ่มจาก 0 จึงต้องบวก 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        if(keynote.equals("1")){
            if (!(title.isNullOrEmpty() && subtitle.isNullOrEmpty())) {
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("title", title)
                    put("subtitle", subtitle)
                    put("color","")
                    put("date",day.toString()+"/"+month.toString()+"/"+year.toString()+" "+hour.toString()+":"+minute.toString())
                }
                val selection = "id = ?"
                val selectionArgs = arrayOf(keynote_id)
                db.update("my_table", values, selection, selectionArgs)
                db.close()


            }

        }else{
            if (!(title.isNullOrEmpty() && subtitle.isNullOrEmpty())) {
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("title", title)
                    put("subtitle", subtitle)
                    put("color","")
                    put("date",day.toString()+"/"+month.toString()+"/"+year.toString()+" "+hour.toString()+":"+minute.toString())
                }

                db.insert("my_table", null, values)
                db.close()


            }

        }



    }

    fun showbottom_color() {

        val view: View = layoutInflater.inflate(R.layout.bottom_color, null)

            val bottomSheetDialog = BottomSheetDialog(this, R.style.SheetDialog)





        val tablayout : TabLayout  = view.findViewById(R.id.tablayout2)
        val viewpager2 : ViewPager = view.findViewById(R.id.viewpager2)
        setupViewPager(viewpager2)

        bottomSheetDialog.setOnShowListener {
            // do something
        }
        bottomSheetDialog.setOnDismissListener {
            // do something
        }
        val bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // do something
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // do something
            }
        }
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.show()
    }
    private fun setupViewPager(viewpager: ViewPager) {
        var adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)


        adapter.addFragment(Fragment_color(), resources.getString(R.string.color))
        adapter.addFragment(Fragment_backgroundcolor(), resources.getString(R.string.background))

        viewpager.setAdapter(adapter)
    }

    class ViewPagerAdapter : FragmentPagerAdapter {

        private final var fragmentList1: ArrayList<Fragment> = ArrayList()
        private final var fragmentTitleList1: ArrayList<String> = ArrayList()


        constructor(supportFragmentManager: FragmentManager)
                : super(supportFragmentManager)

        override fun getItem(position: Int): Fragment {
            return fragmentList1.get(position)
        }


        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList1.get(position)
        }

        override fun getCount(): Int {
            return fragmentList1.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList1.add(fragment)
            fragmentTitleList1.add(title)
        }
    }


}