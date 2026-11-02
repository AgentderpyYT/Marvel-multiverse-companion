package com.example.data.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class SessionEntryType {
  NOTE,
  ROLL,
  GENERATOR,
  CHARACTER_EVENT
}

data class SessionLogEntry(
  val id: String = UUID.randomUUID().toString(),
  val timestamp: Long = System.currentTimeMillis(),
  val title: String,
  val content: String,
  val type: SessionEntryType = SessionEntryType.NOTE,
  val metadata: String? = null
) {
  val formattedTime: String
    get() {
      val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
      return sdf.format(Date(timestamp))
    }
}
