package com.work.workhubpro.models

import javax.inject.Inject

data class User @Inject
constructor(
    val username: String,
    val email: String,
    val password: String,
    val id : Int? =null,
    val ID: Int? = null,
    val role: String?=null,
)

