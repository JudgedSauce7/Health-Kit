package com.example.minorprojecthealthcare

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import com.mayank.minor_project_final.Reminder
import kotlinx.android.synthetic.main.activity_set_reminder.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class SetReminder : AppCompatActivity() {
    private var time1:Calendar?=null
    private var time2:Calendar?=null
    private var time3:Calendar?=null
    private var startday : Calendar ?= null
    private var lastday : Calendar?=null



    private val am by lazy {
        getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_reminder)

        pick_time1.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)
                show_time1.text = SimpleDateFormat("hh:mm a").format(cal.time)
                time1 = cal
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(
                Calendar
                    .MINUTE), DateFormat.is24HourFormat(this)).show()
        }

        pick_time2.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)
                show_time2.text = SimpleDateFormat("hh:mm a").format(cal.time)
                time2 = cal
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(
                Calendar
                    .MINUTE), DateFormat.is24HourFormat(this)).show()
        }

        pick_time3.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)
                show_time3.text = SimpleDateFormat("hh:mm a").format(cal.time)
                time3 = cal
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(
                Calendar
                    .MINUTE), DateFormat.is24HourFormat(this)).show()
        }

        calender_start.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val  day = cal.get(Calendar.DAY_OF_MONTH)
            startday = cal
            Log.i("reminder",SimpleDateFormat("dd/MM/yyyy").format(startday!!.time))
            val datepicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    startday!!.set(year,month,dayOfMonth)
                    Log.i("reminder",SimpleDateFormat("dd/MM/yyyy").format(startday!!.time))
                    start_date.text = ("$dayOfMonth/${month+1}/$year")
                },year,month,day)
            datepicker.show()

        }
        calender_stop.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val  day = cal.get(Calendar.DAY_OF_MONTH)
            lastday = cal
            Log.i("reminder",SimpleDateFormat("dd/MM/yyyy").format(lastday!!.time))
            val datepicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    lastday!!.set(year,month,dayOfMonth)
                    Log.i("reminder",SimpleDateFormat("dd/MM/YYYY").format(lastday!!.time))
                    stop_date.text = ("$dayOfMonth/${month+1}/$year")
                },year,month,day)
            datepicker.show()
        }
        add.setOnClickListener {
            val timelist = arrayListOf<Calendar>()
            if(time1==null && time2==null && time3==null){
                Toast.makeText(this,"Please select atleast one time to take medicine",Toast.LENGTH_LONG).show()
            }else{
                if(time1!=null)
                {
                    timelist.add(time1!!)
                }
                if(time2!=null)
                {
                    timelist.add(time2!!)
                }
                if(time3!=null)
                {
                    timelist.add(time3!!)
                }
            }
            if(startday==null || lastday==null)
            {
                Toast.makeText(this,"Select Dates",Toast.LENGTH_LONG).show()
            }
            else{
                val rem_list = arrayListOf<Reminder>()
                val datelist = arrayListOf<Calendar>()
                datelist.add(startday!!)
                val date = startday!!
                while(date.before(lastday)){
                    date.add(Calendar.DAY_OF_MONTH,1)
                    Log.i("reminder","date"+SimpleDateFormat("dd/MM/yyyy").format(date.time))
                    Log.i("reminder", "Datelist size $datelist.size.toString()")
                    datelist.add(date)

                }
                for(dates in datelist){
                    for(time in timelist){
                        time.set(Calendar.SECOND,0)
                        time.set(Calendar.MILLISECOND,0)
                        val intent = Intent(this@SetReminder,AlarmReceiver::class.java)
                        intent.putExtra("medname",text_med_name.text.toString())
                        intent.putExtra("description",description_input.text.toString())
                        val pendingIntent = PendingIntent.getBroadcast(this@SetReminder,100,intent,
                            PendingIntent.FLAG_UPDATE_CURRENT)
                        Log.i("reminder","Alarm set up")
                        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time.timeInMillis,pendingIntent)
                        Log.i("reminder","date"+SimpleDateFormat("dd/MM/yyyy").format(date.time))
                        Log.i("reminder","time"+SimpleDateFormat("hh:mm a").format(time.time))
                        val remind = Reminder(id = null,
                            medicineName = text_med_name.text.toString(),
                            description = description_input.text.toString(),
                            day = SimpleDateFormat("dd").format(date.time),
                            month = SimpleDateFormat("MM").format(date.time),
                            time = SimpleDateFormat("hh:mm a").format(time.time)
                        )
                        rem_list.add(remind)
                        Log.i("reminder","remind added to list")
                    }
                }
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra("remlist",rem_list as Serializable)
                Log.i("reminder","mainactivity called")
                startActivity(intent)
            }
        }

        cancel.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            Log.i("reminder","cancel button clicked")
            startActivity(intent)

        }
    }
}
