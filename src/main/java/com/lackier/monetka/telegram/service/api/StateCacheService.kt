package com.lackier.monetka.telegram.service.api

import com.lackier.monetka.telegram.dto.GroupAdd
import com.lackier.monetka.telegram.dto.GroupEdit
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.external.dto.enum.GroupType
import java.util.*

interface StateCacheService {
    fun cache(chatId: String, state: State)
    fun cacheGroupAdd(chatId: String, type: GroupType?, name: String?)
    fun uncacheGroupAdd(chatId: String)
    fun getGroupAdd(chatId: String): GroupAdd?
    fun getState(chatId: String): State
    fun hasGroupAdd(chatId: String): Boolean
    fun getGroupEdit(chatId: String): GroupEdit?
    fun cacheGroupEdit(chatId: String, id: UUID)
    fun cacheGroupEdit(chatId: String, type: GroupType)
    fun cacheGroupEdit(chatId: String, name: String)
    fun uncacheGroupEdit(chatId: String)
    fun hasGroupEdit(chatId: String): Boolean
}