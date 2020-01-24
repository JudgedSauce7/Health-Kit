package com.example.minorprojecthealthcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayank.minor_project_final.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var reminder_list = arrayListOf<Reminder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbhelper = SQLiteHelper(this)
        val reminderlist = dbhelper.writableDatabase

        val adapter = ReminderAdapter(reminder_list,this)
        rvreminder.layoutManager = LinearLayoutManager(this)
        rvreminder.adapter = adapter



        reminder_list = ReminderTable.getAllTask(reminderlist)
        adapter.notifyDataSetChanged()
        add_reminder.setOnClickListener{
            val intent = Intent(this,SetReminder::class.java)
            Log.i("reminder","setReminderActivityCalled")
            startActivity(intent)
        }

        Barcode_Scanner.setOnClickListener {
            val intent = Intent(this,Barcode_activity::class.java)
            startActivity(intent)
        }
        predictor.setOnClickListener {

            val intent = Intent(this,check_activity::class.java)
            startActivity(intent)
        }


        adapter.reminderItemClickListener = object : ReminderItemClickListener {
            override fun onRemoveClick(reminder: Reminder, position: Int) {
                ReminderTable.deleteTask(reminderlist,reminder.id!!)
                reminder_list.removeAt(position)
                adapter.updateTasks(ReminderTable.getAllTask(reminderlist))
            }
        }

        val bundle = intent.extras
        var remlist = bundle?.getSerializable("remlist") as? ArrayList<Reminder>
        if(remlist!=null)
        {
            for (item in remlist){
                ReminderTable.insertreminder(reminderlist,item)
                Log.i("reminder","reminder inserted to the database")
                adapter.updateTasks(ReminderTable.getAllTask(reminderlist))
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onBackPressed() {

        val longbackpressed = System.currentTimeMillis()
        if(longbackpressed +2000 > System.currentTimeMillis())
        {
            super.onBackPressed()
            return;
        }else{
            Toast.makeText(baseContext,"Press back again to exit",Toast.LENGTH_SHORT).show()

        }

    }
}
