package com.lackier.monetka.telegram.external.dto

import java.time.ZonedDateTime
import java.util.*

data class Transaction(
    val id: UUID,
    val name: String,
    val category: Category,
    var value: Double,
    val createdAt: ZonedDateTime
)
