package com.lackier.monetka.telegram.external.impl

import com.lackier.monetka.telegram.external.api.MonetkaApiClient
import com.lackier.monetka.telegram.external.dto.Group
import com.lackier.monetka.telegram.external.dto.Transaction
import org.jeasy.random.EasyRandom
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.util.*


@Service
class MonetkaApiClientStub : MonetkaApiClient {
    private val generator = EasyRandom()
    private val defaultPageable: Pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdDate")))

    override fun getGroups(userId: UUID): Page<Group> {
        return PageImpl(listOf(getGroup(), getGroup(), getGroup(), getGroup(), getGroup()), defaultPageable, 5)
    }

    override fun getGroups(userId: UUID, pageNumber: Int): Page<Group> {
        return PageImpl(listOf(getGroup(), getGroup(), getGroup(), getGroup(), getGroup()), getPageable(pageNumber), 5)
    }

    private fun getPageable(pageNumber: Int): Pageable {
        return PageRequest.of(pageNumber, 10, Sort.by(Sort.Order.desc("createdDate")))
    }

    private fun getGroup(): Group {
        return generator.nextObject(Class.forName("com.lackier.monetka.telegram.external.dto.Group")) as Group
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

    override fun createGroup(group: Group) {
        println(group)
        //TODO
    }

    override fun editGroup(group: Group) {
        println(group)
        //TODO
    }

    override fun getGroup(id: UUID): Group {
        return getGroup()
    }

    override fun deleteGroup(chatId: String, id: UUID) {
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