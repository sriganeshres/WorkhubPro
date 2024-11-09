package com.work.workhubpro.models

import javax.inject.Inject

data class SignupResponse @Inject
    constructor(
        val user: User?=null,
        val token:String
    )
