package com.example.week3criticalthinking

sealed class Item {
    data class TaskItem(val task: TaskObj?) : Item()
    data class RoutineItem(val routine: Routine?) : Item()
    data class TimeBlockItem(val timeBlock: TimeBlock?) : Item()
}