package com.work.workhubpro.models

import android.provider.ContactsContract.CommonDataKinds.Email

data class SendMail(
    val email: String,
    val code : Int
)
