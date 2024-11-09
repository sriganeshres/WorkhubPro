package com.work.workhubpro.models

import javax.inject.Inject

data class LoginResponse @Inject
    constructor(
        val token:String,
        val user : User
    )
