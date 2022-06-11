package com.lackier.monetka.telegram.handler

import com.lackier.monetka.telegram.enum.ButtonPressed
import com.lackier.monetka.telegram.enum.QueryParts
import com.lackier.monetka.telegram.enum.State
import com.lackier.monetka.telegram.external.api.MonetkaApiClient
import com.lackier.monetka.telegram.external.enum.GroupType
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
            data == ButtonPressed.GROUPS.path -> groups(chatId)
            data.startsWith(ButtonPressed.GROUPS.path + QueryParts.PAGE_QUERY.path) -> groupsPage(chatId, data)
            data.startsWith(ButtonPressed.GROUPS.path + QueryParts.ID_QUERY.path) -> group(chatId, data)
            data.startsWith(ButtonPressed.GROUPS.path + QueryParts.ADD_QUERY.path) -> groupAdd(chatId, data)
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

    private fun statistics(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.STATISTICS)
        val message = SendMessage(chatId, ButtonPressed.STATISTICS.text)
        message.replyMarkup = inlineKeyboardService.statistics()//TODO
        return message
    }

    private fun expense(chatId: String, data: String): Nothing? {
        stateCacheService.cache(chatId, State.EXPENSES)
        val id = getId(data)
        return null//TODO
    }

    private fun expensesPage(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.EXPENSES)
        val message = SendMessage(chatId, ButtonPressed.EXPENSES.text)
        message.replyMarkup =
            inlineKeyboardService.expenses(apiClient.getTodayExpenses(UUID.randomUUID(), getPageNumber(data)))
        return message
    }

    private fun expenses(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.EXPENSES)
        val message = SendMessage(chatId, ButtonPressed.EXPENSES.text)
        message.replyMarkup = inlineKeyboardService.expenses(apiClient.getTodayExpenses(UUID.randomUUID()))
        return message
    }

    private fun income(chatId: String, data: String): Nothing? {
        stateCacheService.cache(chatId, State.INCOMES)
        val id = getId(data)
        return null//TODO
    }

    private fun incomesPage(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.INCOMES)
        val message = SendMessage(chatId, ButtonPressed.INCOMES.text)
        message.replyMarkup =
            inlineKeyboardService.incomes(apiClient.getTodayIncomes(UUID.randomUUID(), getPageNumber(data)))
        return message
    }

    private fun incomes(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.INCOMES)
        val message = SendMessage(chatId, ButtonPressed.INCOMES.text)
        message.replyMarkup = inlineKeyboardService.incomes(apiClient.getTodayIncomes(UUID.randomUUID()))
        return message
    }

    private fun groupAdd(chatId: String, data: String): SendMessage? {
        stateCacheService.cache(chatId, State.GROUP_ADD)
        val cached = stateCacheService.getGroupAdd(chatId)
        return if (cached == null) {
            stateCacheService.cacheGroupAdd(chatId, null, null)
            val message = SendMessage(chatId, "Choose the type of new group:")
            message.replyMarkup = inlineKeyboardService.chooseGroupType()
            message
        } else if (data.contains(QueryParts.GROUP_TYPE.path)) {
            stateCacheService.cacheGroupAdd(chatId, getGroupType(data), null)
            SendMessage(chatId, "Enter the name of new group:")
        } else {
            null
        }
    }

    private fun group(chatId: String, data: String): Nothing? {
        stateCacheService.cache(chatId, State.GROUPS)
        val id = getId(data)
        return null//TODO
    }

    private fun groupsPage(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.GROUPS)
        val message = SendMessage(chatId, ButtonPressed.GROUPS.text)
        message.replyMarkup =
            inlineKeyboardService.groups(apiClient.getGroups(UUID.randomUUID(), getPageNumber(data)))
        return message
    }

    private fun groups(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.GROUPS)
        val message = SendMessage(chatId, ButtonPressed.GROUPS.text)
        message.replyMarkup = inlineKeyboardService.groups(apiClient.getGroups(UUID.randomUUID()))
        return message
    }

    private fun settings(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.SETTINGS)
        val message = SendMessage(chatId, ButtonPressed.SETTINGS.text)
        message.replyMarkup = inlineKeyboardService.menu()
        return message
    }

    private fun menu(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.MENU)
        val message = SendMessage(chatId, ButtonPressed.MENU.text)
        message.replyMarkup = inlineKeyboardService.menu()
        return message
    }

    private fun getPageNumber(data: String): Int {
        val pageNumber = data.substringAfter(QueryParts.PAGE_QUERY.path)
        return if (pageNumber.isEmpty()) 0 else pageNumber.toInt()
    }

    private fun getId(data: String): UUID {
        return UUID.fromString(data.substringAfter(QueryParts.ID_QUERY.path))
    }

    private fun getGroupType(data: String): GroupType {
        return GroupType.valueOf(data.substringAfter(QueryParts.GROUP_TYPE.path))
    }
}