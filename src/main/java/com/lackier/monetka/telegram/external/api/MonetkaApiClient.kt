package com.lackier.monetka.telegram.external.api

import com.lackier.monetka.telegram.external.dto.Category
import com.lackier.monetka.telegram.external.dto.Transaction
import org.springframework.data.domain.Page
import java.util.*

interface MonetkaApiClient {
    fun getCategories(userId: UUID): Page<Category>
    fun getCategories(userId: UUID, pageNumber: Int): Page<Category>
    fun getCategory(id: UUID): Category
    fun createCategory(category: Category)
    fun editCategory(category: Category)
    fun deleteCategory(chatId: String, id: UUID)
    fun getTodayExpenses(userId: UUID): Page<Transaction>
    fun getTodayExpenses(userId: UUID, pageNumber: Int): Page<Transaction>
    fun getTodayIncomes(userId: UUID): Page<Transaction>
    fun getTodayIncomes(userId: UUID, pageNumber: Int): Page<Transaction>
}
