package com.example.week3criticalthinking

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(val taskViewModel: TaskViewModel, var allItems: List<Item?>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        //var test = allItems[position]
        //Log.d("Get Items", test.toString())

        return when (allItems[position]) {
            is Item.TaskItem -> 0
            is Item.RoutineItem -> 1
            is Item.TimeBlockItem -> 2
            null -> TODO()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            0 -> {
                val view = inflater.inflate(R.layout.fragment_task_container, parent, false)
                //Log.d("onCreateViewHolder", "Task View")
                TaskViewHolder(view)
            }

            1 -> {
                val view = inflater.inflate(R.layout.fragment_routine_container, parent, false)
                //Log.d("onCreateViewHolder", "Routine View")
                RoutineViewHolder(view, taskViewModel)
            }

            2 -> {
                val view = inflater.inflate(R.layout.framgment_timeblock_container, parent, false)
                //Log.d("onCreateViewHolder", "TimeBlock View")
                TimeBlockViewHolder(view, taskViewModel)
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

                holder.deleteButton.setOnClickListener {
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

                holder.bind(rItem)
            }
            is TimeBlockViewHolder -> {
                val item = allItems[position]
                var tbItem = (item as Item.TimeBlockItem).timeBlock

                holder.deleteButtonTimeBlock.setOnClickListener{
                    taskViewModel.deleteTimeBlock(tbItem)
                }

                holder.bind(tbItem)
            }
        }

    }

    override fun getItemCount(): Int {
        return allItems.size
    }

}