package com.example.week3criticalthinking

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.week3criticalthinking.databinding.FragmentNewRoutineSheetBinding
import com.example.week3criticalthinking.databinding.FragmentNewTaskSheetBinding
import com.example.week3criticalthinking.databinding.FragmentNewTimeblockBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.Duration

class NewTimeBlockSheet(private var taskViewModel: TaskViewModel) : DialogFragment()  {
    private lateinit var fragBinding: FragmentNewTimeblockBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var sendSTime: LocalTime = LocalTime.of(0,0,0)
        var sendETime: LocalTime = LocalTime.of(0,0,0)

        fragBinding.buttonStartTime.setOnClickListener {
            lifecycleScope.launch {
                var time = getTime()
                sendSTime = time
                fragBinding.buttonStartTime.text = sendSTime.toString()
            }

        }

        fragBinding.buttonEndTime.setOnClickListener {
            lifecycleScope.launch {
                var time = getTime()
                sendETime = time
                fragBinding.buttonEndTime.text = sendETime.toString()
            }
        }


        fragBinding.saveTimeBlockButton.setOnClickListener{
            saveAction(sendSTime, sendETime)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_new_timeblock, container, false)
        return fragBinding.root
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getTime(): LocalTime = suspendCancellableCoroutine { continuation ->
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        TimePickerDialog(activity,
            { view, hourOfDay, minuteOf ->
                //selectedTimeTV.setText("$hourOfDay:$minute")
                val time = LocalTime.of(hourOfDay, minuteOf)
                continuation.resume(time, null)
            },  hour, minute, false).show()
    }

    private fun saveAction(start: LocalTime, end: LocalTime) {
        val name = fragBinding.name.text.toString()

        val emptyTaskList: MutableList<TaskObj?> = mutableListOf()
        val emptyRoutineList: MutableList<Routine?> = mutableListOf()

        val timeBlock = TimeBlock(name, emptyTaskList, emptyRoutineList, start, end)
        taskViewModel.addTimeBlock(timeBlock)

        fragBinding.name.setText("")
        dismiss()
    }
}