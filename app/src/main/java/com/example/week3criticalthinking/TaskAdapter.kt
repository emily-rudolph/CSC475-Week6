package com.example.week3criticalthinking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: List<TaskObj>, val taskViewModel: TaskViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var allItems: List<Item> = listOf()

    init {
        getAllItems()
    }

    private fun getAllItems() {
        var taskList = tasks.map { Item.TaskItem(it!!) }

        allItems = taskList
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

            else -> throw IllegalArgumentException("Bad View Type")
        }
    }

    override fun getItemCount(): Int = tasks.size

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

                holder.deleteButton.setOnClickListener {
                    taskViewModel.deleteTask(tItem)
                }

                holder.bind(tItem)
            }
        }
    }
}
