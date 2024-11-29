package com.example.week3criticalthinking

import kotlinx.serialization.Transient
import java.time.LocalDateTime

class Routine (
    var name: String,
    @Transient var taskObjs: MutableList<TaskObj?>,
    var timeBlock: String?
)

