package com.example.week3criticalthinking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RoutineTaskAdapter(private val routines: List<Routine?>, private val taskObjs: List<TaskObj?>, var taskViewModel: TaskViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var allItems: List<Item> = listOf()

    init {
        getAllItems()
    }

    private fun getAllItems() {
        var taskList = taskObjs.map { Item.TaskItem(it!!) }
        var routineList = routines.map { Item.RoutineItem(it) }

        allItems = (taskList.plus(routineList))
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (allItems[position]) {
            is Item.TaskItem -> 0
            is Item.RoutineItem -> 1
            is Item.TimeBlockItem -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            0 -> {
                val view = inflater.inflate(R.layout.fragment_task_container, parent, false)
                TaskViewHolder(view)
            }

            1 -> {
                val view = inflater.inflate(R.layout.fragment_routine_container, parent, false)
                RoutineViewHolder(view, taskViewModel)
            }
            else -> throw IllegalArgumentException("Bad View Type")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is TaskViewHolder -> {
                val item = allItems[position]
                var tItem = (item as Item.TaskItem).task

                holder.checkBox.isChecked = tItem?.isCompleted() == true

                holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                    taskViewModel.completeTask(tItem, isChecked)
                }

                holder.deleteButton.setOnClickListener{
                    taskViewModel.deleteTask(tItem)
                }

                holder.bind(tItem)
            }
            is RoutineViewHolder -> {
                val item = allItems[position]
                var rItem = (item as Item.RoutineItem).routine

                holder.deleteButtonRoutine.setOnClickListener{
                    taskViewModel.deleteRoutine(rItem)
                }

                holder.bind((allItems[position] as Item.RoutineItem).routine)
            }
        }
    }
    override fun getItemCount(): Int = routines.size + taskObjs.size
}