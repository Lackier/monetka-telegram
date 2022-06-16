package com.lackier.monetka.telegram.external.dto

import com.lackier.monetka.telegram.external.dto.enum.CategoryType
import java.time.ZonedDateTime
import java.util.*

data class Category(var id: UUID?, val chatId: String, val name: String, val type: CategoryType, val createdAt: ZonedDateTime)
