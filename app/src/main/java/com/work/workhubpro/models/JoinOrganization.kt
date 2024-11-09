package com.work.workhubpro.models

import javax.inject.Inject

data class JoinOrganization @Inject
    constructor(
        val useremail: String,
        val key: Int = 0
    )
