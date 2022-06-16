package com.lackier.monetka.telegram.service.api

import com.lackier.monetka.backend.api.enums.CategoryTypeDto
import com.lackier.monetka.telegram.dto.CategoryAdd
import com.lackier.monetka.telegram.dto.CategoryEdit
import com.lackier.monetka.telegram.dto.enum.State
import java.util.*

interface StateCacheService {
    fun cache(chatId: String, state: State)
    fun cacheCategoryAdd(chatId: String, type: CategoryTypeDto?, name: String?)
    fun uncacheCategoryAdd(chatId: String)
    fun getCategoryAdd(chatId: String): CategoryAdd?
    fun getState(chatId: String): State
    fun hasCategoryAdd(chatId: String): Boolean
    fun getCategoryEdit(chatId: String): CategoryEdit?
    fun cacheCategoryEdit(chatId: String, id: UUID)
    fun cacheCategoryEdit(chatId: String, type: CategoryTypeDto)
    fun cacheCategoryEdit(chatId: String, name: String)
    fun uncacheCategoryEdit(chatId: String)
    fun hasCategoryEdit(chatId: String): Boolean
}