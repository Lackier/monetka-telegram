package com.lackier.monetka.telegram.external.impl

import com.lackier.monetka.backend.api.client.ExpenseApiClient
import com.lackier.monetka.backend.api.dto.CategoryDto
import com.lackier.monetka.backend.api.dto.TransactionDto
import com.lackier.monetka.backend.api.dto.request.TransactionCreateRequest
import com.lackier.monetka.backend.api.dto.request.TransactionUpdateRequest
import org.jeasy.random.EasyRandom
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.util.*


@Service
class ExpenseApiClientStub : ExpenseApiClient {
    private val generator = EasyRandom()
    private val defaultPageable: Pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")))

    override fun getTodayExpenses(userId: UUID): Page<TransactionDto> {
        return PageImpl(
            listOf(
                getExpense(), getExpense(), getExpense(), getExpense(), getExpense(),
                getExpense(), getExpense(), getExpense(), getExpense(), getExpense()
            ), defaultPageable, 27
        )
    }

    override fun getTodayExpenses(userId: UUID, pageNumber: Int): Page<TransactionDto> {
        return PageImpl(
            listOf(
                getExpense(), getExpense(), getExpense(), getExpense(), getExpense(),
                getExpense(), getExpense(), getExpense(), getExpense(), getExpense()
            ), getPageable(pageNumber), 27
        )
    }

    override fun getExpense(id: UUID): TransactionDto {
        return getExpense()
    }

    override fun createExpense(expenseDto: TransactionCreateRequest) {
        println(expenseDto)
    }

    override fun updateExpense(transactionUpdateRequest: TransactionUpdateRequest) {
        println(transactionUpdateRequest)
    }

    override fun deleteExpense(chatId: String, id: UUID) {
        println(chatId + id)
    }

    override fun getExpenseCategories(chatId: String): List<CategoryDto> {
        return listOf(
            getCategory(), getCategory(), getCategory(), getCategory(), getCategory(), getCategory(),
            getCategory(), getCategory(), getCategory(), getCategory(), getCategory()
        )
    }

    private fun getCategory(): CategoryDto {
        return generator.nextObject(Class.forName("com.lackier.monetka.backend.api.dto.CategoryDto")) as CategoryDto
    }

    private fun getExpense(): TransactionDto {
        val transaction = getTransaction()
        if (transaction.value > 0) {
            transaction.value *= -1
        }
        if (transaction.value == 0.0) {
            transaction.value -= 10
        }
        return transaction
    }

    private fun getTransaction(): TransactionDto {
        return generator.nextObject(Class.forName("com.lackier.monetka.backend.api.dto.TransactionDto")) as TransactionDto
    }

    private fun getPageable(pageNumber: Int): Pageable {
        return PageRequest.of(pageNumber, 10, Sort.by(Sort.Order.desc("createdDate")))
    }
}