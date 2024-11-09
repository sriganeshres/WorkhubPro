package com.work.workhubpro.models

import javax.inject.Inject

data class Resp @Inject
constructor(
    val success: Boolean,
    val msg : String,
)