package com.android.attendme.cloudStore

import com.android.attendme.dataClasses.AttendanceRecordData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AttendanceRecord {
    interface OnAttendanceRecordFetchComplete{
        fun onSuccess(list : ArrayList<AttendanceRecordData>)
        fun onFailure(message : String)
    }
    interface OnAttendanceRecordStoreComplete{
        fun onSuccess()
        fun onFailure(message : String)
    }
    interface OnIsEmptyCheckComplete{
        fun onRecordEmpty()
        fun onRecordNotEmpty()
        fun onErrorOccurs(message : String)
    }
    private val TAG = "AttendanceRecord"
    private lateinit var executor : ExecutorService
    fun fetchAttendanceRecord(n : Int,
                          listener : OnAttendanceRecordFetchComplete
    ){
        executor = Executors.newSingleThreadExecutor()
        executor.execute {
            //Implement this
        }
    }
    fun storeAttendanceRecord(data : AttendanceRecordData,
                              listener: OnAttendanceRecordStoreComplete
    ){
        executor = Executors.newSingleThreadExecutor()
        executor.execute {
            //Implement this
        }
    }
    fun isEmpty(listener : OnIsEmptyCheckComplete){
        executor = Executors.newSingleThreadExecutor()
        executor.execute {
            //Implement this
        }
    }
}