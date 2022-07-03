package com.lackier.monetka.telegram.service.impl

import com.lackier.monetka.backend.api.client.ExpenseApiClient
import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.Query
import com.lackier.monetka.telegram.dto.enum.QueryParts
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
import com.lackier.monetka.telegram.service.api.ExpenseService
import com.lackier.monetka.telegram.service.api.HelperService
import com.lackier.monetka.telegram.service.api.StateCacheService
import lombok.AccessLevel
import lombok.experimental.FieldDefaults
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ExpenseServiceImpl(
    private val stateCacheService: StateCacheService,
    private val expenseApiClient: ExpenseApiClient,
    private val inlineKeyboardService: InlineKeyboardService,
    private val helperService: HelperService
) : ExpenseService {
    override fun expenses(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.EXPENSES)
        val message = SendMessage(chatId, ButtonPressed.EXPENSES.text)
        message.replyMarkup = inlineKeyboardService.expenses(expenseApiClient.getTodayExpenses(UUID.randomUUID()))
        return message
    }

    override fun expensesPage(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.EXPENSES)
        val message = SendMessage(chatId, ButtonPressed.EXPENSES.text)
        message.replyMarkup =
            inlineKeyboardService.expenses(
                expenseApiClient.getTodayExpenses(
                    UUID.randomUUID(),
                    helperService.getPageNumber(data)
                )
            )
        return message
    }

    override fun expense(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.EXPENSES)
        val id = helperService.getId(data)
        val message = SendMessage(chatId, ButtonPressed.EXPENSES.text)
        message.replyMarkup = inlineKeyboardService.expense(expenseApiClient.getExpense(id))
        return message
    }

    override fun expenseAdd(chatId: String, data: String): SendMessage? {
        stateCacheService.cache(chatId, State.EXPENSE_ADD)
        val cached = stateCacheService.getExpenseAdd(chatId)
        return if (cached == null) {
            stateCacheService.cacheExpenseAdd(chatId)
            val message = SendMessage(chatId, "Choose the category of new expense:")
            message.replyMarkup = inlineKeyboardService.chooseExpenseCategory(
                expenseApiClient.getExpenseCategories(chatId),
                Query.ADD_EXPENSE
            )
            message
        } else if (data.contains(QueryParts.EXPENSE_CATEGORY.path)) {
            stateCacheService.cacheExpenseAdd(chatId, getCategoryId(data))
            SendMessage(chatId, "Enter the name of new expense:")
        } else {
            null
        }
    }

    override fun expenseEdit(chatId: String, data: String): SendMessage? {
        stateCacheService.cache(chatId, State.EXPENSE_EDIT)
        val cached = stateCacheService.getExpenseEdit(chatId)
        return if (cached == null) {
            stateCacheService.cacheExpenseEdit(chatId, id = helperService.getId(data), categoryId = null)
            val message = SendMessage(chatId, "Choose the category of expense:")
            message.replyMarkup = inlineKeyboardService.chooseExpenseCategory(
                expenseApiClient.getExpenseCategories(chatId),
                Query.EDIT_EXPENSE
            )
            message
        } else if (data.contains(QueryParts.EXPENSE_CATEGORY.path)) {
            stateCacheService.cacheExpenseEdit(chatId, categoryId = getCategoryId(data), id = null)
            SendMessage(chatId, "Enter new name of expense:")
        } else {
            null
        }
    }

    override fun expenseDelete(chatId: String, data: String): SendMessage {
        expenseApiClient.deleteExpense(chatId, helperService.getId(data))
        stateCacheService.cache(chatId, State.EXPENSES)
        return expenses(chatId)
    }

    private fun getCategoryId(data: String): UUID {
        return UUID.fromString(data.substringAfter(QueryParts.INCOME_CATEGORY.path))
    }
}