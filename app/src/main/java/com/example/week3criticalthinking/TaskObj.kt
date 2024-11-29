package com.example.week3criticalthinking

import java.time.LocalDateTime
import kotlin.time.Duration

class  TaskObj (
    var name: String,
    var dateTimeDue: LocalDateTime?,
    var routine: String?,
    var timeBlock: String?,
    var timeDuration: Duration,
    var completed: Boolean,
) {
    fun isCompleted(): Boolean {
        return completed
    }
}
