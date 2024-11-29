package com.example.week3criticalthinking

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.time.DurationUnit

// Task ViewHolder
class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val taskName: TextView = itemView.findViewById(R.id.checkBox)
    private val duration: TextView = itemView.findViewById(R.id.durationText)
    private val scheduled: TextView = itemView.findViewById(R.id.scheduledText)

    val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    val deleteButton: ImageButton = itemView.findViewById<ImageButton>(R.id.deleteButton)

    fun bind(task: TaskObj?) {

        val hours = task?.timeDuration?.div(60)?.toInt(DurationUnit.MINUTES)
        val minutes = task?.timeDuration?.toInt(DurationUnit.MINUTES)?.rem(60)

        val durationText = "${hours}h ${minutes}m"

        duration.text = "Duration: " + durationText
        scheduled.text = "Scheduled: " + task?.dateTimeDue
        taskName.text = task?.name
    }
}

// Routine ViewHolder
class RoutineViewHolder(view: View, var taskViewModel: TaskViewModel) : RecyclerView.ViewHolder(view) {
    private val routineName: TextView = view.findViewById(R.id.textRoutineName)
    private val nestedRecyclerView: RecyclerView = view.findViewById(R.id.recyclerViewRoutine)

    val deleteButtonRoutine: ImageButton = itemView.findViewById<ImageButton>(R.id.deleteButtonRoutine)

    fun bind(routine: Routine?) {
        routineName.text = routine?.name

        Log.d("RoutineBinding", "Routine name: ${routine?.name}, Tasks count: ${routine?.taskObjs?.size}")
        // If the routine contains tasks, setup a nested RecyclerView
        val tasks = routine?.taskObjs?: listOf()
        nestedRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
        nestedRecyclerView.adapter = TaskAdapter(tasks as List<TaskObj>, taskViewModel)
    }
}

// TimeBlock ViewHolder
class TimeBlockViewHolder(view: View, var taskViewModel: TaskViewModel) : RecyclerView.ViewHolder(view) {
    private val timeStart: TextView = view.findViewById(R.id.editStartTime)
    private val timeEnd: TextView = view.findViewById(R.id.editEndTime)
    private val name: TextView = view.findViewById(R.id.textTimeBlockName)
    private val nestedRecyclerView: RecyclerView = view.findViewById(R.id.recyclerViewTimeBlock)

    val deleteButtonTimeBlock: ImageButton = itemView.findViewById<ImageButton>(R.id.deleteButtonTimeBlock)

    fun bind(timeBlock: TimeBlock?) {
        timeStart.text = timeBlock?.startTime.toString()
        timeEnd.text = timeBlock?.endTime.toString()
        name.text = timeBlock?.name

        // TimeBlock's nested items (Routines and Tasks)
        val routines = timeBlock?.routines?: listOf()
        val tasks = timeBlock?.taskObjs?: listOf()
        nestedRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
        nestedRecyclerView.adapter = RoutineTaskAdapter(routines, tasks, taskViewModel)
    }
}
