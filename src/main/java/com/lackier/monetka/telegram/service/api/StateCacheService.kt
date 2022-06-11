package com.lackier.monetka.telegram.service.api

import com.lackier.monetka.telegram.dto.GroupAdd
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.external.dto.enum.GroupType

interface StateCacheService {
    fun cache(chatId: String, state: State)
    fun cacheGroupAdd(chatId: String, type: GroupType?, name: String?)
    fun uncacheGroupAdd(chatId: String)
    fun getGroupAdd(chatId: String): GroupAdd?
    fun getState(chatId: String): State
    fun hasGroupAdd(chatId: String): Boolean
}