package com.android.attendme.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.attendme.Helpers.Helpers
import com.android.attendme.R
import com.android.attendme.cloudStore.AttendanceRecord
import com.android.attendme.dataClasses.AttendanceRecordData
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class MyAttendance : AppCompatActivity() {
    class MyAdapter(list : ArrayList<AttendanceRecordData>,
                    c : Context,
    ) : BaseAdapter(){
        private val context : Context = c
        private val arrayList = list
        override fun getCount(): Int {
            return arrayList.size
        }
        override fun getItem(index: Int): Any {
            return arrayList[index]
        }
        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }
        override fun getView(position: Int, convertView: View?, group: ViewGroup?): View {
            val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view =
                convertView ?: inflator.inflate(R.layout.my_attendance_list_item, group, false)
            val record = arrayList[position]
            val dateTV = view.findViewById<TextView>(R.id.my_attendance_list_item_date)
            val timeTV = view.findViewById<TextView>(R.id.my_attendance_list_item_time)
            dateTV.text = String.format("%s : %s",context.getString(R.string.date),record.date)
            timeTV.text = String.format("%s : %s",context.getString(R.string.time),record.time)
            return view
        }

    }
    abstract class LazyLoader : AbsListView.OnScrollListener{
        private val DEFAULT_THRESHOLD : Int = 15
        private var isLoading : Boolean = true
        private var previousTotal : Int = 0
        var threshold = DEFAULT_THRESHOLD

        override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
           //No need to implement
        }

        override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
            if(isLoading){
                if(totalItemCount > previousTotal){
                    isLoading = false
                    previousTotal = totalItemCount
                }
            }
            if(!isLoading && ((firstVisibleItem + visibleItemCount) >= (totalItemCount - threshold))){
                isLoading = true
                loadMoreItem(view, firstVisibleItem, visibleItemCount, totalItemCount)
            }
        }
       abstract fun loadMoreItem(view: AbsListView?,
                                 firstVisibleItem: Int,
                                 visibleItemCount: Int,
                                 totalItemCount: Int)

    }
    class NoAttendanceRecord(db : AttendanceRecord) : Fragment(){
        private val dataBase = db
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.my_attendance_no_attendance_data, container, false)
            val btn = view.findViewById<TextView>(R.id.my_attendance_start_mark_attendance_btn)
            btn.setOnClickListener {
                //Mark attendance
            }
            return view
        }
    }
    class MyAttendanceRecord(db : AttendanceRecord) : Fragment(){
        private val dataBase = db
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.my_attendance_list_view_fragment, container, false)
            val btn = view.findViewById<ExtendedFloatingActionButton>(R.id.my_attendance_mark_attendance_btn)
            val listView = view.findViewById<ListView>(R.id.my_attendance_data_list_view)
            AttendanceRecord().fetchAttendanceRecord(20,
                object : AttendanceRecord.OnAttendanceRecordFetchComplete{
                    override fun onSuccess(list: ArrayList<AttendanceRecordData>) {
                        val adapter = ArrayAdapter(view.context,
                            R.layout.my_attendance_list_item,list)
                    }

                    override fun onFailure(message: String) {

                    }

                })
            btn.setOnClickListener {
                //Mark attendance
            }
            return view
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_attendance)
        val dataBase = AttendanceRecord()
        dataBase.isEmpty(object : AttendanceRecord.OnIsEmptyCheckComplete{
            override fun onRecordEmpty() {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.my_attendance_frame_layout, NoAttendanceRecord(dataBase))
                transaction.commit()
            }

            override fun onRecordNotEmpty() {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.my_attendance_frame_layout, MyAttendanceRecord(dataBase))
                transaction.commit()
            }

            override fun onErrorOccurs(message: String) {
                Helpers.makeToast(applicationContext, "Unable to fetch attendance data")
            }

        })
    }
}