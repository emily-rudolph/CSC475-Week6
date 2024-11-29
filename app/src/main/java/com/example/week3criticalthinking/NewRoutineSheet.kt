package com.example.week3criticalthinking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.example.week3criticalthinking.databinding.FragmentNewRoutineSheetBinding

class NewRoutineSheet (private var taskViewModel: TaskViewModel) : DialogFragment()  {
    private lateinit var fragBinding: FragmentNewRoutineSheetBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var timeBlock: TimeBlock? = null

        //Get Time Block Spinner Set Up
        var listT = ArrayList<String>()
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


        //Get value from Time Block Spinner
        fragBinding.spinnerTimeBlock.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var selectedTimeBlock: String? = null
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedTimeBlock = listT[position]

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


        fragBinding.saveButtonRoutine.setOnClickListener{
            saveAction(timeBlock)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_new_routine_sheet, container, false)
        return fragBinding.root
    }

    private fun saveAction(timeBlock: TimeBlock?) {
        val name = fragBinding.name.text.toString()
        val emptyTaskList: MutableList<TaskObj?> = mutableListOf()

        val routine = Routine(name, emptyTaskList, timeBlock?.name)
        Log.d("NewRoutine", routine.toString())
        taskViewModel.addRoutine(routine)

        fragBinding.name.setText("")
        dismiss()

    }
}