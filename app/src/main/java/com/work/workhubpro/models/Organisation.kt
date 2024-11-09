package com.work.workhubpro.models

import javax.inject.Inject

data class Organisation @Inject
constructor(
    val name: String,
    val description : String,
    val adminname:String,
    val domain: String,
    val privacy_key: Int?=0,
    val users: List<User> = emptyList(),
    val ID : Int?=0,
)