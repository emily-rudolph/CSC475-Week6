package com.example.week3criticalthinking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.week3criticalthinking.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class NewTaskSheet(private var taskViewModel: TaskViewModel) : DialogFragment() {

    private lateinit var fragBinding: FragmentNewTaskSheetBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dateTimeDue: LocalDateTime? = null
        var routine: Routine? = null
        var timeBlock: TimeBlock? = null
        var duration: Duration = Duration.ZERO

        var hours: Int = 0
        var minutes: Int = 0

        //Get Number Pickers Set Up
        val hoursPicker = fragBinding.numberPickerHours
        hoursPicker.maxValue = 24
        hoursPicker.minValue = 0

        //Get Number Pickers Set Up
        val minutesPicker = fragBinding.numberPickerMinutes
        minutesPicker.maxValue = 60
        minutesPicker.minValue = 0

        //Get Routine Spinner Set Up
        var listR = ArrayList<String?>()
        for (i in taskViewModel.routineList) {
            listR.add(i!!.name)
        }
        listR.add("None")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listR
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fragBinding.spinnerRoutine.adapter = adapter

        fragBinding.spinnerRoutine.setSelection(listR.indexOf("None"))



        //Get Time Block Spinner Set Up
        var listT = ArrayList<String?>()

        for (i in taskViewModel.timeBlockList) {
            listT.add(i!!.name)
        }
        listT.add("None")

        val adapter2 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listT
        )

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fragBinding.spinnerTimeBlock.adapter = adapter2

        fragBinding.spinnerTimeBlock.setSelection(listT.indexOf("None"))


        //Get Due Date
        fragBinding.addDueDateButton.setOnClickListener {
            lifecycleScope.launch {
                var date = getDate()
                var time = getTime()

                Log.d("Date", date.toString())
                Log.d("Time", time.toString())
                dateTimeDue = LocalDateTime.of(date, time)
            }

            fragBinding.addDueDateButton.setImageResource(R.drawable.baseline_check_24)

        }

        //Get Value From Routine Spinner
        fragBinding.spinnerRoutine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var selectedRoutine: String? = null

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRoutine = listR[position]
                Log.d("OnItem Select Routine", selectedRoutine.toString())

                if (selectedRoutine == "None") {
                    routine = null
                }

                for (i in taskViewModel.routineList) {
                    if (i!!.name == selectedRoutine) {
                        //Log.d("Inside If Statement Routine Name", i.name)
                        routine = i
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedRoutine = null
            }
        }

        //Get Value From TimeBlock Spinner
        fragBinding.spinnerTimeBlock.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var selectedTimeBlock: String? = null


            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Log.d("Time Block: Size", listT.size.toString())
                //Log.d("Time Block: Position", position.toString())


                selectedTimeBlock = listT[position]
                Log.d("OnItem Select Time Block", selectedTimeBlock.toString())

                if (selectedTimeBlock == "None") {
                    timeBlock = null
                }

                for (i in taskViewModel.timeBlockList) {
                    if (i?.name == selectedTimeBlock) {
                        timeBlock = i
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedTimeBlock = null
            }
        }


        //get values from number pickers
        hoursPicker.setOnValueChangedListener {
                numberPicker, oldVal, newVal ->
                hours = hoursPicker.value
                Log.d("NewTaskHour", hours.toString())
        }

        minutesPicker.setOnValueChangedListener {
                numberPicker, oldVal, newVal ->
                minutes = minutesPicker.value
                Log.d("NewTaskMin", minutes.toString())
        }

        //Log.d("NewTaskMin", minutes.toString())
        //Log.d("NewTaskHour", hours.toString())

        //Call Save Function
        fragBinding.saveButton.setOnClickListener{
            saveAction(dateTimeDue, routine, timeBlock, hours, minutes)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_new_task_sheet, container, false)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getDate(): LocalDate = suspendCancellableCoroutine { continuation ->
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                //Remember to set text in screen
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                continuation.resume(selectedDate, null)
            }, year, month, day
        ).show()
    }

    private fun saveAction(due : LocalDateTime?, routine: Routine?, timeBlock: TimeBlock?, hours: Int, minutes: Int) {
        val name = fragBinding.name.text.toString()

        Log.d("SaveTaskRoutine", routine?.name.toString())
        Log.d("SaveTaskTime", timeBlock?.name.toString())
        Log.d("SaveTask", name)

        if (routine?.timeBlock != timeBlock?.name && routine != null && timeBlock != null) {
            CustomAlert(requireContext(), "Conflicting Routine and TimeBlocks")
            return
        }

        Log.d("SaveTaskRT", routine?.timeBlock.toString())
        Log.d("SaveTaskT", timeBlock?.name.toString())
        Log.d("SaveTask", name)

        val temp = hours * 60 + minutes
        val duration = temp.toDuration(DurationUnit.MINUTES)

        val newTask = TaskObj(name, due, routine?.name, timeBlock?.name, duration ,false)
        taskViewModel.addTask(newTask)

        fragBinding.name.setText("")
        dismiss()
    }

    private fun newTaskDisplay(){
        fragBinding.newTaskText.text = "New Task"
    }

}