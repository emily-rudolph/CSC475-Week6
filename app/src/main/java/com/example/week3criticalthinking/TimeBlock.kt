package com.example.week3criticalthinking

import androidx.lifecycle.MutableLiveData
import java.time.LocalTime

class TimeBlock (
    var name: String,
    @Transient var taskObjs:MutableList<TaskObj?>,
    @Transient var routines:MutableList<Routine?>,
    var startTime: LocalTime,
    var endTime: LocalTime

)