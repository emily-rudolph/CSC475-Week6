package com.example.week3criticalthinking


import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week3criticalthinking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var myRepository: ViewModelRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        //mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        myRepository = ViewModelRepository(this)
        taskViewModel = ViewModelProvider(
            this, TaskViewModelFactory(myRepository, applicationContext)
        )[TaskViewModel::class.java]

        val buttonClickTask = mainBinding.taskAddButton
        buttonClickTask.setOnClickListener {
            NewTaskSheet(taskViewModel).show(supportFragmentManager, "newTask")
        }

        val buttonClickRoutine = mainBinding.taskRoutineButton
        buttonClickRoutine.setOnClickListener {
            NewRoutineSheet(taskViewModel).show(supportFragmentManager, "newRoutine")
        }

        val buttonClickTimeBlock = mainBinding.taskTimeBlockButton
        buttonClickTimeBlock.setOnClickListener {
            NewTimeBlockSheet(taskViewModel).show(supportFragmentManager, "newTimeBlock")
        }

        makeRecycleView()

    }

    fun makeRecycleView(){
        taskViewModel.filteredObservableData.observe(this){
            mainBinding.recyclerView.apply {
                Log.d("Main Activity", "Applying Update")
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = MainAdapter(taskViewModel, taskViewModel.filteredObservableData.value!!)
            }
        }
    }
}


