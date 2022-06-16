package com.lackier.monetka.telegram.external.api

import com.lackier.monetka.telegram.external.dto.Group
import com.lackier.monetka.telegram.external.dto.Transaction
import org.springframework.data.domain.Page
import java.util.*

interface MonetkaApiClient {
    fun getGroups(userId: UUID): Page<Group>
    fun getGroups(userId: UUID, pageNumber: Int): Page<Group>
    fun getGroup(id: UUID): Group
    fun createGroup(group: Group)
    fun editGroup(group: Group)
    fun deleteGroup(chatId: String, id: UUID)
    fun getTodayExpenses(userId: UUID): Page<Transaction>
    fun getTodayExpenses(userId: UUID, pageNumber: Int): Page<Transaction>
    fun getTodayIncomes(userId: UUID): Page<Transaction>
    fun getTodayIncomes(userId: UUID, pageNumber: Int): Page<Transaction>
}
