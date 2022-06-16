package com.lackier.monetka.telegram.external.impl

import com.lackier.monetka.telegram.external.api.MonetkaApiClient
import com.lackier.monetka.telegram.external.dto.Category
import com.lackier.monetka.telegram.external.dto.Transaction
import org.jeasy.random.EasyRandom
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.util.*


@Service
class MonetkaApiClientStub : MonetkaApiClient {
    private val generator = EasyRandom()
    private val defaultPageable: Pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdDate")))

    override fun getCategories(userId: UUID): Page<Category> {
        return PageImpl(
            listOf(getCategory(), getCategory(), getCategory(), getCategory(), getCategory()),
            defaultPageable,
            5
        )
    }

    override fun getCategories(userId: UUID, pageNumber: Int): Page<Category> {
        return PageImpl(
            listOf(getCategory(), getCategory(), getCategory(), getCategory(), getCategory()),
            getPageable(pageNumber),
            5
        )
    }

    private fun getPageable(pageNumber: Int): Pageable {
        return PageRequest.of(pageNumber, 10, Sort.by(Sort.Order.desc("createdDate")))
    }

    private fun getCategory(): Category {
        return generator.nextObject(Class.forName("com.lackier.monetka.telegram.external.dto.Category")) as Category
    }

    override fun getTodayExpenses(userId: UUID): Page<Transaction> {
        return PageImpl(
            listOf(
                getExpense(), getExpense(), getExpense(), getExpense(), getExpense(),
                getExpense(), getExpense(), getExpense(), getExpense(), getExpense()
            ), defaultPageable, 27
        )
    }

    override fun getTodayExpenses(userId: UUID, pageNumber: Int): Page<Transaction> {
        return PageImpl(
            listOf(
                getExpense(), getExpense(), getExpense(), getExpense(), getExpense(),
                getExpense(), getExpense(), getExpense(), getExpense(), getExpense()
            ), getPageable(pageNumber), 27
        )
    }

    private fun getExpense(): Transaction {
        val transaction = getTransaction()
        if (transaction.value > 0) {
            transaction.value *= -1
        }
        if (transaction.value == 0.0) {
            transaction.value -= 10
        }
        return transaction
    }

    override fun getTodayIncomes(userId: UUID): Page<Transaction> {
        return PageImpl(listOf(getIncome(), getIncome(), getIncome()), defaultPageable, 3)
    }

    override fun getTodayIncomes(userId: UUID, pageNumber: Int): Page<Transaction> {
        return PageImpl(listOf(getIncome(), getIncome(), getIncome()), getPageable(pageNumber), 3)
    }

    override fun createCategory(category: Category) {
        println(category)
        //TODO
    }

    override fun editCategory(category: Category) {
        println(category)
        //TODO
    }

    override fun getCategory(id: UUID): Category {
        return getCategory()
    }

    override fun deleteCategory(chatId: String, id: UUID) {
        println(chatId + id)
        //TODO
    }

    private fun getIncome(): Transaction {
        val transaction = getTransaction()
        if (transaction.value < 0) {
            transaction.value *= -1
        }
        if (transaction.value == 0.0) {
            transaction.value += 10
        }
        return transaction
    }

    private fun getTransaction(): Transaction {
        return generator.nextObject(Class.forName("com.lackier.monetka.telegram.external.dto.Transaction")) as Transaction
    }
}