package com.work.workhubpro.models

import javax.inject.Inject

data class Task @Inject
constructor(
    val ID: Int = 0,
    val name: String,
    val description : String,
    val assigned_to:Int,
    val work_hub_id:Int,
    val assigned_by: Int,
    val project_key: Int,
    val status:String,
)
data class UpdateTask @Inject
constructor(
    val taskID: Int,
    var status: String
) 