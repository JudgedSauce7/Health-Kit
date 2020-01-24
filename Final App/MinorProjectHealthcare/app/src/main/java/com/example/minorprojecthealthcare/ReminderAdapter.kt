package com.mayank.minor_project_final

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minorprojecthealthcare.R
import kotlinx.android.synthetic.main.reminder_item.view.*

class ReminderAdapter(
    val reminder_list:ArrayList<Reminder>,
    val context: Context
):RecyclerView.Adapter<ReminderViewHolder>() {

    lateinit var reminderItemClickListener: ReminderItemClickListener
    fun updateTasks(newReminders: ArrayList<Reminder>) {
        Log.i("reminder","Update task in rv adapter")
        reminder_list.clear()
        reminder_list.addAll(newReminders)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val inflater = LayoutInflater.from(context)
        val view =inflater.inflate(R.layout.reminder_item,parent,false)
        return ReminderViewHolder(view)
    }

    override fun getItemCount(): Int  =reminder_list.size

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {

        val reminder_item = reminder_list[position]
        holder.itemView.apply {
            Log.i("reminder","holder mapping")
            med_name.text = reminder_item.medicineName
            med_description.text = reminder_item.description
            date.text = reminder_item.day
            month.text = reminder_item.month
            time.text = reminder_item.time
        }
        holder.itemView.Remove.setOnClickListener{
            reminderItemClickListener.onRemoveClick(reminder_item,position)
        }
    }

}
class ReminderViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)