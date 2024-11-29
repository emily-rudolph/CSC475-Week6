package com.example.week3criticalthinking

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val taskTabelName = "task_table"
private const val name = "Name"
private const val comp = "Completed"
private const val dueDateTime = "DueDate"
private const val routine = "Routine"
private var timeBlock = "Time_Block"
private const val duration = "Duration"

private const val routineTabelName = "routine_table"
private const val routineName = "Name"
private const val routineTimeBlock = "Time_Block"

private const val timeTabelName = "timeBlock_table"
private const val timeName = "Name"
private const val timeStart = "Start_Time"
private const val timeEnd = "EndTime"

/*   var name: String,
    var dateTimeDue: LocalDateTime?,
    var routine: Routine?,
    var timeBlock: TimeBlock?,
    var timeDuration: Duration,
    var completed: Boolean,*/


class DataBaseHelper(var context: Context) : SQLiteOpenHelper(context, "taskList.db", null, 1) {
    //val gson = GsonProvider.gson

    override fun onCreate(db: SQLiteDatabase) {
        val createTaskTableStatement: String = "CREATE TABLE $taskTabelName ($name TEXT PRIMARY KEY,$dueDateTime TEXT NOT NULL ,$routine TEXT, $timeBlock TEXT, $duration INTEGER, $comp TEXT)"
        db.execSQL(createTaskTableStatement)

        val createRoutineTableStatement: String = "CREATE TABLE $routineTabelName ($routineName TEXT PRIMARY KEY,$routineTimeBlock TEXT)"
        db.execSQL(createRoutineTableStatement)

        val createTimeBlockTableStatement: String = "CREATE TABLE $timeTabelName ($timeName TEXT PRIMARY KEY,$timeStart TEXT ,$timeEnd TEXT)"
        db.execSQL(createTimeBlockTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addTask(taskObj: TaskObj) : Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        var cv: ContentValues = ContentValues()

        cv.put(name, taskObj.name)
        cv.put(dueDateTime, taskObj.dateTimeDue.toString())
        cv.put(routine, taskObj.routine)
        cv.put(timeBlock, taskObj.timeBlock)
        cv.put(duration, taskObj.timeDuration.toInt(DurationUnit.MINUTES))
        cv.put(comp, taskObj.completed.toString())

        val success: Long = db.insert(taskTabelName,null,cv)
        val l: Long = -1

        return success != l
    }

   /* fun getTask(taskObj: TaskObj) : Boolean {
        val db: SQLiteDatabase = this.readableDatabase
        val l: Long = -1
        return success != l
    } */

    fun getAllTasks() : MutableList<TaskObj?> {
        val db: SQLiteDatabase = this.readableDatabase

        var list: MutableList<TaskObj?> = mutableListOf()
        val queryString: String = "SELECT * FROM $taskTabelName"
        val cursor: Cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val name: String = cursor.getString(0)
                val getDue: String? = cursor.getString(1)
                val routine : String? = cursor.getString(2)
                val timeBlock : String? = cursor.getString(3)
                val duration : Int = cursor.getInt(4)
                var getCompleted: String? = cursor.getString(5)


                var due: LocalDateTime? = null
                if (getDue != "null") {
                    due = LocalDateTime.parse(getDue)
                }

                var completed: Boolean = false
                if (!getCompleted.isNullOrBlank() && getCompleted != "null") {
                    completed = getCompleted.toBoolean()
                }

                val task: TaskObj = TaskObj(name, due, routine, timeBlock, duration.toDuration(DurationUnit.MINUTES), completed)
                list.add(task)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return list
    }

    fun deleteTask(taskObj: TaskObj?): Boolean {
        val db: SQLiteDatabase = this.readableDatabase
        val ident: String = taskObj!!.name
        val success: Int = db.delete(taskTabelName, "$name = ?", arrayOf(ident))

        return success != -1
    }

    fun addRoutine(routine: Routine): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        var cv: ContentValues = ContentValues()

        cv.put(routineName, routine.name)
        cv.put(routineTimeBlock, routine.timeBlock)

        val success: Long = db.insert(routineTabelName,null,cv)
        val l: Long = -1

        return success != l

    }

    fun getAllRoutines(): MutableList<Routine?> {
        val db: SQLiteDatabase = this.readableDatabase

        var list: MutableList<Routine?> = mutableListOf()
        val queryString: String = "SELECT * FROM $routineTabelName"
        val cursor: Cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val name: String = cursor.getString(0)
                val timeBlock : String? = cursor.getString(1)

                var tasklist: MutableList<TaskObj?> = mutableListOf()

                var currentList = getAllTasks()
                for (i in currentList){
                    if (i?.routine == name) {
                        tasklist.add(i)
                    }
                }

                val routine: Routine = Routine(name, tasklist, timeBlock)
                list.add(routine)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return list

    }

    fun deleteRoutine(routine: Routine?): Boolean {
        val db: SQLiteDatabase = this.readableDatabase
        val ident: String = routine!!.name
        val success: Int = db.delete(routineTabelName, "$routineName = ?", arrayOf(ident))

        return success != -1
    }

    fun addTimeBlock(timeBlock: TimeBlock): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        var cv: ContentValues = ContentValues()

        cv.put(timeName, timeBlock.name)
        cv.put(timeStart, timeBlock.startTime.toString())
        cv.put(timeEnd, timeBlock.endTime.toString())

        val success: Long = db.insert(timeTabelName,null,cv)
        val l: Long = -1

        return success != l

    }

    fun getAllTimeBlocks(): MutableList<TimeBlock?> {
        val db: SQLiteDatabase = this.readableDatabase

        var list: MutableList<TimeBlock?> = mutableListOf()
        val queryString: String = "SELECT * FROM $timeTabelName"
        val cursor: Cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val name: String = cursor.getString(0)
                val getStart: String? = cursor.getString(1)
                val getEnd: String? = cursor.getString(2)

                val start = LocalTime.parse(getStart)
                val end = LocalTime.parse(getEnd)

                var tasklist: MutableList<TaskObj?> = mutableListOf()
                var currentList = getAllTasks()
                for (i in currentList){
                    if (i?.timeBlock == name && i.routine == null) {
                        tasklist.add(i)
                    }
                }


                var routineList: MutableList<Routine?> = mutableListOf()
                var currentList2 = getAllRoutines()
                for (i in currentList2){
                    if (i!!.timeBlock == name) {
                        routineList.add(i)
                    }
                }


                val timeBlock: TimeBlock = TimeBlock(name, tasklist, routineList, start, end)
                list.add(timeBlock)

            } while (cursor.moveToNext())
        }

        cursor.close()

        return list
    }

    fun deleteTimeBlock(timeBlock: TimeBlock?): Boolean {
        val db: SQLiteDatabase = this.readableDatabase
        val ident: String = timeBlock!!.name
        val success: Int = db.delete(timeTabelName, "$timeName = ?", arrayOf(ident))

        return success != -1
    }

}