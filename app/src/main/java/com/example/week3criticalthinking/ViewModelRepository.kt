package com.example.week3criticalthinking

import android.content.Context
import androidx.lifecycle.MutableLiveData

class ViewModelRepository(context: Context) {
    var dbHelper: DataBaseHelper = DataBaseHelper(context)
    var tasks: MutableList<TaskObj?> = dbHelper.getAllTasks()
    var routines:  MutableList<Routine?> = dbHelper.getAllRoutines()
    var timeBlocks: MutableList<TimeBlock?> = dbHelper.getAllTimeBlocks()
}