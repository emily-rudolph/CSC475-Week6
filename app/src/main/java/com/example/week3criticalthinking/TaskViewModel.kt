package com.example.week3criticalthinking

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TaskViewModel(myRepository: ViewModelRepository, context: Context): ViewModel() {

    var tasksList: MutableList<TaskObj?> = myRepository.tasks
    var routineList: MutableList<Routine?> = myRepository.routines
    var timeBlockList: MutableList<TimeBlock?> = myRepository.timeBlocks
    val databasehelper: DataBaseHelper = DataBaseHelper(context)


    //var filteredData = getFilteredDate()
    val filteredObservableData: MutableLiveData<MutableList<Item?>> = MutableLiveData()

    init {

        filteredObservableData.value = getFilteredDate()
    }

    private fun updateFilteredObservableData() {
        filteredObservableData.value = getFilteredDate()
    }

    init{
        Log.d("TaskViewModel", "Pulling From Database")
    }

    fun getFilteredDate(): MutableList<Item?> {
        var filteredTasks = mutableListOf<TaskObj?>()
        var filteredRoutines = mutableListOf<Routine?>()
        var filteredTimeBlocks = mutableListOf<TimeBlock?>()

        for (i in tasksList) {
            if(i?.routine == null && i?.timeBlock == null) {
                filteredTasks.add(i)
            }
        }

        for (i in routineList) {
            if(i?.timeBlock == null){
                filteredRoutines.add(i)
            }
        }

        for (i in timeBlockList) {
            filteredTimeBlocks.add(i)
        }

        var filteredTaskItems = filteredTasks.map { Item.TaskItem(it) } ?: emptyList()
        var filteredRoutineItems = filteredRoutines.map { Item.RoutineItem(it) } ?: emptyList()
        var filteredTimeBlockItems = filteredTimeBlocks.map { Item.TimeBlockItem(it) } ?: emptyList()

        Log.d("TaskViewModel", "GetFilteredDataSize" + filteredTaskItems.size.toString())
        return (filteredTaskItems + filteredRoutineItems + filteredTimeBlockItems) as MutableList<Item?>

    }



    fun addTask(taskObj: TaskObj){
        databasehelper.addTask(taskObj)
        tasksList.add(taskObj)


        if (taskObj.routine != null) {
            for (i in routineList) {
                if (i!!.name == taskObj.routine!!) {
                    i.taskObjs.add(taskObj)

                    if (i.timeBlock != null) {
                        for (j in timeBlockList) {
                            if (j?.name == i.timeBlock) {
                                val nestedRoutine = j?.routines?.find { it?.name == i.name}
                                nestedRoutine?.taskObjs?.add(taskObj)
                            }
                        }
                    }
                }
            }
        }

        if (taskObj.timeBlock != null && taskObj.routine == null) {
            for (i in timeBlockList) {
                if (i!!.name == taskObj.timeBlock!!) {
                    i.taskObjs.add(taskObj)
                }
            }
        }


        //filteredData = getFilteredDate()
        updateFilteredObservableData()
        Log.d("TaskViewModel", "Finshed Adding Task")

    }

    fun deleteTask(taskObj: TaskObj?){
        databasehelper.deleteTask(taskObj)

        tasksList.remove(taskObj)

        if (taskObj?.routine != null) {
            for (i in routineList) {
                if (i!!.name == taskObj.routine!!) {
                    i.taskObjs.remove(taskObj)

                    if (i.timeBlock != null) {
                        for (j in timeBlockList) {
                            if (j?.name == i.timeBlock) {
                                val nestedRoutine = j?.routines?.find { it?.name == i.name}
                                nestedRoutine?.taskObjs?.remove(taskObj)
                            }
                        }
                    }
                }
            }
        }

        if (taskObj?.timeBlock != null) {
            for (i in timeBlockList) {
                if (i!!.name == taskObj.timeBlock!!) {
                    i.taskObjs.remove(taskObj)
                }
            }
        }

        //filteredData = getFilteredDate()
        updateFilteredObservableData()
    }

    fun completeTask(taskObj: TaskObj?, isChecked: Boolean) {
        if (isChecked) {
            taskObj?.completed = true
        } else {
            taskObj?.completed = false
        }
    }

    fun addRoutine(routine: Routine) {
        databasehelper.addRoutine(routine)

        Log.d("TaskViewModel AddRoutine", "After Database Call")
        routineList.add(routine)

        for (i in timeBlockList){
            if (i!!.name == routine.timeBlock) {
                i.routines.add(routine)
            }
        }

        //filteredData = getFilteredDate()
        updateFilteredObservableData()
    }

    fun deleteRoutine(routine: Routine?) {
        databasehelper.deleteRoutine(routine)

        routineList.remove(routine)

        for (i in routine!!.taskObjs) {
            if (i?.timeBlock != null) {
                val time = timeBlockList.find {it?.name == i.timeBlock }
                time?.taskObjs?.add(i)
            }
        }

        for (i in tasksList) {
            if (i?.routine == routine.name) {
                i.routine = null
            }
        }

        for (i in timeBlockList){
            if (i!!.name == routine.timeBlock) {
                i.routines.remove(routine)
            }
        }

        //filteredData = getFilteredDate()
        updateFilteredObservableData()
    }

    fun addTimeBlock(timeBlock: TimeBlock) {
        databasehelper.addTimeBlock(timeBlock)
        timeBlockList.add(timeBlock)

        //filteredData = getFilteredDate()
        updateFilteredObservableData()

    }

    fun deleteTimeBlock(timeBlock: TimeBlock?) {
        databasehelper.deleteTimeBlock(timeBlock)

        timeBlockList.remove(timeBlock)

        for (i in tasksList) {
            if (i?.timeBlock == timeBlock?.name) {
                i?.timeBlock = null
            }
        }

        for (i in routineList) {
            if (i!!.timeBlock == timeBlock?.name) {
               i.timeBlock = null
            }
        }

        //filteredData = getFilteredDate()
        updateFilteredObservableData()
    }
}

class TaskViewModelFactory(private val myRepository: ViewModelRepository, private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(myRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

