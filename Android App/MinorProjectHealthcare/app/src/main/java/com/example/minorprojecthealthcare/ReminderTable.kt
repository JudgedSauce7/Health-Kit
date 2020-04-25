package com.mayank.minor_project_final

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class ReminderTable {
    companion object {
        val TABLE_NAME = "remindertable"

        val CMD_CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            medicine TEXT,
            description TEXT,
            day TEXT,
            month TEXT,
            time TEXT
            );
        """.trimIndent()

        fun insertreminder(db:SQLiteDatabase,reminder: Reminder){
            val row = ContentValues()
            row.put("medicine",reminder.medicineName)
            row.put("description",reminder.description)
            row.put("day",reminder.day)
            row.put("month",reminder.month)
            row.put("time",reminder.time)

            db.insert(TABLE_NAME,null,row)
        }

        fun getAllTask(db:SQLiteDatabase): ArrayList<Reminder> {

            val reminders = arrayListOf<Reminder>()

            val cursor = db.query(
                TABLE_NAME,
                arrayOf("id","medicine", "description", "day", "month", "time"),
                null,
                null,
                null,
                null,
                null
            )

            cursor.moveToFirst()
            val idcol = cursor.getColumnIndexOrThrow("id")
            val medicinecol = cursor.getColumnIndexOrThrow("medicine")
            val descriptioncol = cursor.getColumnIndexOrThrow("description")
            val daycol = cursor.getColumnIndexOrThrow("day")
            val monthcol = cursor.getColumnIndexOrThrow("month")
            val timecol = cursor.getColumnIndexOrThrow("time")
            while (cursor.moveToNext()) {
                val remind = Reminder(
                    cursor.getInt(idcol),
                    cursor.getString(medicinecol),
                    cursor.getString(descriptioncol),
                    cursor.getString(daycol),
                    cursor.getString(monthcol),
                    cursor.getString(timecol)
                )
                reminders.add(remind)
            }
            cursor.close()
            return reminders
        }

        fun deleteTask(db:SQLiteDatabase,id:Int) {
            db.delete(TABLE_NAME,"id = $id",null)
        }
    }
}