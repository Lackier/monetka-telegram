package com.lackier.monetka.telegram.external.dto

import com.lackier.monetka.telegram.external.dto.enum.GroupType
import java.time.ZonedDateTime
import java.util.*

data class Group(var id: UUID?, val chatId: String, val name: String, val type: GroupType, val createdAt: ZonedDateTime)
