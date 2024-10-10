package com.sol.news.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(date: String): String {
    val zonedDateTime = ZonedDateTime.parse(date)
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)
    return zonedDateTime.format(formatter)
}