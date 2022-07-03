package com.lackier.monetka.telegram.external.impl;

import com.lackier.monetka.backend.api.client.IncomeApiClient
import com.lackier.monetka.backend.api.dto.CategoryDto
import com.lackier.monetka.backend.api.dto.TransactionDto
import com.lackier.monetka.backend.api.dto.request.TransactionCreateRequest
import com.lackier.monetka.backend.api.dto.request.TransactionUpdateRequest
import org.jeasy.random.EasyRandom
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class IncomeApiClientStub : IncomeApiClient {
    private val generator = EasyRandom()
    private val defaultPageable: Pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")))

    override fun getTodayIncomes(userId: UUID): Page<TransactionDto> {
        return PageImpl(listOf(getIncome(), getIncome(), getIncome()), defaultPageable, 3)
    }

    override fun getTodayIncomes(userId: UUID, pageNumber: Int): Page<TransactionDto> {
        return PageImpl(listOf(getIncome(), getIncome(), getIncome()), getPageable(pageNumber), 3)
    }

    override fun getIncome(id: UUID): TransactionDto {
        return getIncome()
    }

    override fun createIncome(incomeRequest: TransactionCreateRequest) {
        println(incomeRequest)
    }

    override fun updateIncome(transactionUpdateRequest: TransactionUpdateRequest) {
        println(transactionUpdateRequest)
    }

    override fun deleteIncome(chatId: String, id: UUID) {
        println(chatId + id)
    }

    override fun getIncomeCategories(chatId: String): List<CategoryDto> {
        return listOf(
            getCategory(), getCategory(), getCategory(), getCategory(), getCategory(), getCategory(),
            getCategory(), getCategory(), getCategory(), getCategory(), getCategory()
        )
    }

    private fun getCategory(): CategoryDto {
        return generator.nextObject(Class.forName("com.lackier.monetka.backend.api.dto.CategoryDto")) as CategoryDto
    }

    private fun getIncome(): TransactionDto {
        val transaction = getTransaction()
        if (transaction.value < 0) {
            transaction.value *= -1
        }
        if (transaction.value == 0.0) {
            transaction.value += 10
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
