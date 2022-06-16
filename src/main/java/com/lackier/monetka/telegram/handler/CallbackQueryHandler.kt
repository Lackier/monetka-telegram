package com.lackier.monetka.telegram.handler

import com.lackier.monetka.backend.api.client.MonetkaApiClient
import com.lackier.monetka.backend.api.enums.CategoryTypeDto
import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.QueryParts
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
import com.lackier.monetka.telegram.service.api.StateCacheService
import lombok.AccessLevel
import lombok.experimental.FieldDefaults
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import java.util.*

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class CallbackQueryHandler(
    private val inlineKeyboardService: InlineKeyboardService,
    private val stateCacheService: StateCacheService,
    private val apiClient: MonetkaApiClient
) {
    fun processCallbackQuery(buttonQuery: CallbackQuery): BotApiMethod<*>? {
        val chatId = buttonQuery.message.chatId.toString()
        val data = buttonQuery.data!!

        return when {
            data == ButtonPressed.MENU.path -> menu(chatId)
            data == ButtonPressed.SETTINGS.path -> settings(chatId)
            data == ButtonPressed.CATEGORIES.path -> categories(chatId)
            data.startsWith(ButtonPressed.CATEGORIES.path + QueryParts.PAGE_QUERY.path) -> categoriesPage(chatId, data)
            data.startsWith(ButtonPressed.CATEGORIES.path + QueryParts.ID_QUERY.path) -> category(chatId, data)
            data.startsWith(ButtonPressed.CATEGORIES.path + QueryParts.ADD_QUERY.path) -> categoryAdd(chatId, data)
            data.startsWith(ButtonPressed.CATEGORIES.path + QueryParts.EDIT_QUERY.path)
            -> categoryEdit(chatId, data)
            data.startsWith(ButtonPressed.CATEGORIES.path + QueryParts.DELETE_QUERY.path + QueryParts.ID_QUERY.path)
            -> categoryDelete(chatId, data)
            data == ButtonPressed.INCOMES.path -> incomes(chatId)
            data.startsWith(ButtonPressed.INCOMES.path + QueryParts.PAGE_QUERY.path) -> incomesPage(chatId, data)
            data.startsWith(ButtonPressed.INCOMES.path + QueryParts.ID_QUERY.path) -> income(chatId, data)
            data == ButtonPressed.EXPENSES.path -> expenses(chatId)
            data.startsWith(ButtonPressed.EXPENSES.path + QueryParts.PAGE_QUERY.path) -> expensesPage(chatId, data)
            data.startsWith(ButtonPressed.EXPENSES.path + QueryParts.ID_QUERY.path) -> expense(chatId, data)
            data == ButtonPressed.STATISTICS.path -> statistics(chatId)
            else -> default(chatId)
        }
    }

    private fun default(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.DEFAULT)
        return SendMessage(chatId, "Default")
    }

    private fun menu(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.MENU)
        val message = SendMessage(chatId, ButtonPressed.MENU.text)
        message.replyMarkup = inlineKeyboardService.menu()
        return message
    }

    private fun settings(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.SETTINGS)
        val message = SendMessage(chatId, ButtonPressed.SETTINGS.text)
        message.replyMarkup = inlineKeyboardService.menu()
        return message
    }

    private fun categories(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.CATEGORIES)
        val message = SendMessage(chatId, ButtonPressed.CATEGORIES.text)
        message.replyMarkup = inlineKeyboardService.categories(apiClient.getCategories(UUID.randomUUID()))
        return message
    }

    private fun categoriesPage(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.CATEGORIES)
        val message = SendMessage(chatId, ButtonPressed.CATEGORIES.text)
        message.replyMarkup =
            inlineKeyboardService.categories(apiClient.getCategories(UUID.randomUUID(), getPageNumber(data)))
        return message
    }

    private fun categoryAdd(chatId: String, data: String): SendMessage? {
        stateCacheService.cache(chatId, State.CATEGORY_ADD)
        val cached = stateCacheService.getCategoryAdd(chatId)
        return if (cached == null) {
            stateCacheService.cacheCategoryAdd(chatId, null, null)
            val message = SendMessage(chatId, "Choose the type of new category:")
            message.replyMarkup = inlineKeyboardService.chooseCategoryType()
            message
        } else if (data.contains(QueryParts.CATEGORY_TYPE.path)) {
            stateCacheService.cacheCategoryAdd(chatId, getCategoryType(data), null)
            SendMessage(chatId, "Enter the name of new category:")
        } else {
            null
        }
    }

    private fun category(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.CATEGORIES)
        val id = getId(data)
        val message = SendMessage(chatId, ButtonPressed.CATEGORIES.text)
        message.replyMarkup = inlineKeyboardService.category(apiClient.getCategory(id))
        return message
    }

    private fun categoryEdit(chatId: String, data: String): SendMessage? {
        stateCacheService.cache(chatId, State.CATEGORY_EDIT)
        val cached = stateCacheService.getCategoryEdit(chatId)
        return if (cached == null) {
            stateCacheService.cacheCategoryEdit(chatId, getId(data))
            val message = SendMessage(chatId, "Choose the type of category:")
            message.replyMarkup = inlineKeyboardService.chooseCategoryTypeEdit()
            message
        } else if (data.contains(QueryParts.CATEGORY_TYPE.path)) {
            stateCacheService.cacheCategoryEdit(chatId, getCategoryType(data))
            SendMessage(chatId, "Enter new name of category:")
        } else {
            null
        }
    }

    private fun categoryDelete(chatId: String, data: String): SendMessage {
        apiClient.deleteCategory(chatId, getId(data))
        stateCacheService.cache(chatId, State.CATEGORIES)
        return categories(chatId)
    }

    private fun incomes(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.INCOMES)
        val message = SendMessage(chatId, ButtonPressed.INCOMES.text)
        message.replyMarkup = inlineKeyboardService.incomes(apiClient.getTodayIncomes(UUID.randomUUID()))
        return message
    }

    private fun incomesPage(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.INCOMES)
        val message = SendMessage(chatId, ButtonPressed.INCOMES.text)
        message.replyMarkup =
            inlineKeyboardService.incomes(apiClient.getTodayIncomes(UUID.randomUUID(), getPageNumber(data)))
        return message
    }

    private fun income(chatId: String, data: String): Nothing? {
        stateCacheService.cache(chatId, State.INCOMES)
        val id = getId(data)
        return null//TODO
    }

    private fun expenses(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.EXPENSES)
        val message = SendMessage(chatId, ButtonPressed.EXPENSES.text)
        message.replyMarkup = inlineKeyboardService.expenses(apiClient.getTodayExpenses(UUID.randomUUID()))
        return message
    }

    private fun expensesPage(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.EXPENSES)
        val message = SendMessage(chatId, ButtonPressed.EXPENSES.text)
        message.replyMarkup =
            inlineKeyboardService.expenses(apiClient.getTodayExpenses(UUID.randomUUID(), getPageNumber(data)))
        return message
    }

    private fun expense(chatId: String, data: String): Nothing? {
        stateCacheService.cache(chatId, State.EXPENSES)
        val id = getId(data)
        return null//TODO
    }

    private fun statistics(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.STATISTICS)
        val message = SendMessage(chatId, ButtonPressed.STATISTICS.text)
        message.replyMarkup = inlineKeyboardService.statistics()//TODO
        return message
    }

    private fun getPageNumber(data: String): Int {
        val pageNumber = data.substringAfter(QueryParts.PAGE_QUERY.path)
        return if (pageNumber.isEmpty()) 0 else pageNumber.toInt()
    }

    private fun getId(data: String): UUID {
        return UUID.fromString(data.substringAfter(QueryParts.ID_QUERY.path))
    }

    private fun getCategoryType(data: String): CategoryTypeDto {
        return CategoryTypeDto.valueOf(data.substringAfter(QueryParts.CATEGORY_TYPE.path))
    }
}
