package com.work.workhubpro.models

import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date


    data class Message(
        val msg: String,
        val sender: String,
        val receiver: String,
        var CreatedAt: LocalTime?=null,
        val ID: Int?= null,
    )
