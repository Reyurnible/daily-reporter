package io.reyurnible.daily_reporter.model

import org.joda.time.DateTime

data class User(
        val id: Int,
        val accountName: String,
        val passHash: String,
        val authority: Int,
        val delFlg: Boolean,
        val createdAt: DateTime
)