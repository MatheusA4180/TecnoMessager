package com.example.tecnomessager.utils.extension

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object HelperFunctions {

    fun formatDate(date: Date): String =
        SimpleDateFormat("dd/MM/yyyy").format(date)

    fun formatHour(date: Date): String =
        SimpleDateFormat("hh:mm").format(date)

}
