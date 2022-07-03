package com.lackier.monetka.telegram.service.api

import com.lackier.monetka.backend.api.dto.TransactionDto
import com.lackier.monetka.backend.api.enums.CategoryTypeDto
import com.lackier.monetka.telegram.dto.*
import com.lackier.monetka.telegram.dto.enum.State
import java.util.*

interface StateCacheService {
    fun cache(chatId: String, state: State)
    fun getState(chatId: String): State

    fun cacheCategoryAdd(chatId: String, type: CategoryTypeDto?, name: String?)
    fun hasCategoryAdd(chatId: String): Boolean
    fun getCategoryAdd(chatId: String): CategoryAdd?
    fun unCacheCategoryAdd(chatId: String)

    fun cacheCategoryEdit(chatId: String, id: UUID)
    fun cacheCategoryEdit(chatId: String, type: CategoryTypeDto)
    fun cacheCategoryEdit(chatId: String, name: String)
    fun hasCategoryEdit(chatId: String): Boolean
    fun getCategoryEdit(chatId: String): CategoryEdit?
    fun unCacheCategoryEdit(chatId: String)

    fun cacheIncomeAdd(chatId: String)
    fun cacheIncomeAdd(chatId: String, categoryId: UUID)
    fun cacheIncomeAdd(chatId: String, incomeAdd: IncomeAdd)
    fun hasIncomeAdd(chatId: String): Boolean
    fun getIncomeAdd(chatId: String): IncomeAdd?
    fun unCacheIncomeAdd(chatId: String)

    fun cacheIncomeEdit(chatId: String, id: UUID?, categoryId: UUID?)
    fun cacheIncomeEdit(chatId: String, incomeEditDto: IncomeEdit)
    fun hasIncomeEdit(chatId: String): Boolean
    fun getIncomeEdit(chatId: String): IncomeEdit?

    fun unCacheIncomeEdit(chatId: String)
    fun cacheExpenseAdd(chatId: String)
    fun cacheExpenseAdd(chatId: String, categoryId: UUID)
    fun cacheExpenseAdd(chatId: String, expenseAddDto: ExpenseAdd)
    fun hasExpenseAdd(chatId: String): Boolean
    fun getExpenseAdd(chatId: String): ExpenseAdd?
    fun unCacheExpenseAdd(chatId: String)

    fun cacheExpenseEdit(chatId: String, id: UUID?, categoryId: UUID?)
    fun cacheExpenseEdit(chatId: String, expenseEditDto: ExpenseEdit)
    fun hasExpenseEdit(chatId: String): Boolean
    fun getExpenseEdit(chatId: String): ExpenseEdit?
    fun unCacheExpenseEdit(chatId: String)
}