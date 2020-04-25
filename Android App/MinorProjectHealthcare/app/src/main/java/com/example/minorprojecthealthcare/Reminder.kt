package com.mayank.minor_project_final

import android.os.Parcelable
import java.io.Serializable

data class Reminder(
    var id: Int?,
    var medicineName:String,
    var description:String,
    var day:String,
    var month:String,
    var time:String
) : Serializable