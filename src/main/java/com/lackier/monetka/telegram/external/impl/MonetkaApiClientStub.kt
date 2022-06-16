package com.lackier.monetka.telegram.external.impl

import com.lackier.monetka.backend.api.client.MonetkaApiClient
import com.lackier.monetka.backend.api.dto.CategoryDto
import com.lackier.monetka.backend.api.dto.TransactionDto
import org.jeasy.random.EasyRandom
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.util.*


@Service
class MonetkaApiClientStub : MonetkaApiClient {
    private val generator = EasyRandom()
    private val defaultPageable: Pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")))

    override fun getCategories(userId: UUID): Page<CategoryDto> {
        return PageImpl(
            listOf(getCategory(), getCategory(), getCategory(), getCategory(), getCategory()),
            defaultPageable,
            5
        )
    }

    override fun getCategories(userId: UUID, pageNumber: Int): Page<CategoryDto> {
        return PageImpl(
            listOf(getCategory(), getCategory(), getCategory(), getCategory(), getCategory()),
            getPageable(pageNumber),
            5
        )
    }

    private fun getPageable(pageNumber: Int): Pageable {
        return PageRequest.of(pageNumber, 10, Sort.by(Sort.Order.desc("createdDate")))
    }

    private fun getCategory(): CategoryDto {
        return generator.nextObject(Class.forName("com.lackier.monetka.backend.api.dto.CategoryDto")) as CategoryDto
    }

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

    override fun getTodayIncomes(userId: UUID): Page<TransactionDto> {
        return PageImpl(listOf(getIncome(), getIncome(), getIncome()), defaultPageable, 3)
    }

    override fun getTodayIncomes(userId: UUID, pageNumber: Int): Page<TransactionDto> {
        return PageImpl(listOf(getIncome(), getIncome(), getIncome()), getPageable(pageNumber), 3)
    }

    override fun createCategory(categoryDto: CategoryDto) {
        println(categoryDto)
        //TODO
    }

    override fun editCategory(categoryDto: CategoryDto) {
        println(categoryDto)
        //TODO
    }

    override fun getCategory(id: UUID): CategoryDto {
        return getCategory()
    }

    override fun deleteCategory(chatId: String, id: UUID) {
        println(chatId + id)
        //TODO
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
}