package com.lackier.monetka.telegram.handler

import com.lackier.monetka.backend.api.client.CategoryApiClient
import com.lackier.monetka.backend.api.client.ExpenseApiClient
import com.lackier.monetka.backend.api.client.IncomeApiClient
import com.lackier.monetka.backend.api.dto.CategoryDto
import com.lackier.monetka.backend.api.dto.request.TransactionCreateRequest
import com.lackier.monetka.backend.api.dto.request.TransactionUpdateRequest
import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
import com.lackier.monetka.telegram.service.api.HelperService
import com.lackier.monetka.telegram.service.api.StateCacheService
import lombok.AccessLevel
import lombok.RequiredArgsConstructor
import lombok.experimental.FieldDefaults
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.ZonedDateTime
import java.util.*


@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class MessageHandler(
    private val inlineKeyboardService: InlineKeyboardService,
    private val stateCacheService: StateCacheService,
    private val categoryApiClient: CategoryApiClient,
    private val incomeApiClient: IncomeApiClient,
    private val expenseApiClient: ExpenseApiClient,
    private val helperService: HelperService
) {
    fun answerMessage(message: Message): BotApiMethod<*> {
        val chatId: String = message.chatId.toString()
        val inputText: String = message.text

        return if ((stateCacheService.getState(chatId) == State.CATEGORY_ADD) and stateCacheService.hasCategoryAdd(chatId)) {
            val categoryAdd = stateCacheService.getCategoryAdd(chatId)
            categoryApiClient.createCategory(CategoryDto(
                null, chatId, inputText, categoryAdd?.type!!, ZonedDateTime.now()))

            stateCacheService.unCacheCategoryAdd(chatId)
            stateCacheService.cache(chatId, State.CATEGORIES)
            toCategories(chatId)
        } else if ((stateCacheService.getState(chatId) == State.CATEGORY_EDIT) and stateCacheService.hasCategoryEdit(chatId)) {
            val categoryEdit = stateCacheService.getCategoryEdit(chatId)
            categoryApiClient.editCategory(CategoryDto(
                    categoryEdit?.id, chatId, inputText, categoryEdit?.type!!, ZonedDateTime.now()))

            stateCacheService.unCacheCategoryEdit(chatId)
            stateCacheService.cache(chatId, State.CATEGORIES)
            toCategories(chatId)
        } else if ((stateCacheService.getState(chatId) == State.INCOME_ADD) and stateCacheService.hasIncomeAdd(chatId)) {
            val incomeAdd = stateCacheService.getIncomeAdd(chatId)
            if (incomeAdd!!.name == null) {
                incomeAdd.name = inputText
                stateCacheService.cacheIncomeAdd(chatId, incomeAdd)
                SendMessage(chatId, "Enter how much you earned:")
            } else if (incomeAdd.value == null) {
                incomeAdd.value = inputText.toDouble()
                incomeApiClient.createIncome(TransactionCreateRequest(
                    chatId, incomeAdd.name!!, incomeAdd.category!!, incomeAdd.value!!))

                stateCacheService.unCacheIncomeAdd(chatId)
                stateCacheService.cache(chatId, State.INCOMES)
                toIncomes(chatId)
            } else {
                toIncomes(chatId)
            }
        } else if ((stateCacheService.getState(chatId) == State.INCOME_EDIT) and stateCacheService.hasIncomeEdit(chatId)) {
            val incomeEdit = stateCacheService.getIncomeEdit(chatId)
            if (incomeEdit!!.name == null) {
                incomeEdit.name = inputText
                stateCacheService.cacheIncomeEdit(chatId, incomeEdit)
                SendMessage(chatId, "Enter how much you earned:")
            } else if (incomeEdit.value == null) {
                incomeEdit.value = inputText.toDouble()
                incomeApiClient.updateIncome(TransactionUpdateRequest(
                    incomeEdit.id, chatId, incomeEdit.name!!, incomeEdit.category!!, incomeEdit.value!!))

                stateCacheService.unCacheIncomeEdit(chatId)
                stateCacheService.cache(chatId, State.INCOMES)
                toIncomes(chatId)
            } else {
                toIncomes(chatId)
            }
        } else if ((stateCacheService.getState(chatId) == State.EXPENSE_ADD) and stateCacheService.hasExpenseAdd(chatId)) {
            val expenseAdd = stateCacheService.getExpenseAdd(chatId)
            if (expenseAdd!!.name == null) {
                expenseAdd.name = inputText
                stateCacheService.cacheExpenseAdd(chatId, expenseAdd)
                SendMessage(chatId, "Enter how much you spent:")
            } else if (expenseAdd.value == null) {
                expenseAdd.value = inputText.toDouble()
                expenseApiClient.createExpense(TransactionCreateRequest(
                    chatId, expenseAdd.name!!, expenseAdd.category!!, expenseAdd.value!!))

                stateCacheService.unCacheExpenseAdd(chatId)
                stateCacheService.cache(chatId, State.EXPENSES)
                toExpenses(chatId)
            } else {
                toExpenses(chatId)
            }
        } else if ((stateCacheService.getState(chatId) == State.EXPENSE_EDIT) and stateCacheService.hasExpenseEdit(chatId)) {
            val expenseEdit = stateCacheService.getExpenseEdit(chatId)
            if (expenseEdit!!.name == null) {
                expenseEdit.name = inputText
                stateCacheService.cacheExpenseEdit(chatId, expenseEdit)
                SendMessage(chatId, "Enter how much you spent:")
            } else if (expenseEdit.value == null) {
                expenseEdit.value = inputText.toDouble()
                expenseApiClient.updateExpense(TransactionUpdateRequest(
                    expenseEdit.id, chatId, expenseEdit.name!!, expenseEdit.category!!, expenseEdit.value!!))

                stateCacheService.unCacheExpenseEdit(chatId)
                stateCacheService.cache(chatId, State.EXPENSES)
                toExpenses(chatId)
            } else {
                toExpenses(chatId)
            }
        } else {
            helperService.menu(chatId)
        }
    }

    private fun toCategories(chatId: String): SendMessage {
        val msg = SendMessage(chatId, ButtonPressed.CATEGORIES.text)
        msg.replyMarkup = inlineKeyboardService.categories(categoryApiClient.getCategories(UUID.randomUUID()))
        return msg
    }

    private fun toIncomes(chatId: String): SendMessage {
        val msg = SendMessage(chatId, ButtonPressed.INCOMES.text)
        msg.replyMarkup = inlineKeyboardService.incomes(incomeApiClient.getTodayIncomes(UUID.randomUUID()))
        return msg
    }

    private fun toExpenses(chatId: String): SendMessage {
        val msg = SendMessage(chatId, ButtonPressed.EXPENSES.text)
        msg.replyMarkup = inlineKeyboardService.expenses(expenseApiClient.getTodayExpenses(UUID.randomUUID()))
        return msg
    }
}