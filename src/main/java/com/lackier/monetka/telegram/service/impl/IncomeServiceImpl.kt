package com.lackier.monetka.telegram.service.impl

import com.lackier.monetka.backend.api.client.IncomeApiClient
import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.Query
import com.lackier.monetka.telegram.dto.enum.QueryParts
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
import com.lackier.monetka.telegram.service.api.HelperService
import com.lackier.monetka.telegram.service.api.IncomeService
import com.lackier.monetka.telegram.service.api.StateCacheService
import lombok.AccessLevel
import lombok.experimental.FieldDefaults
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class IncomeServiceImpl(
    private val stateCacheService: StateCacheService,
    private val incomeApiClient: IncomeApiClient,
    private val inlineKeyboardService: InlineKeyboardService,
    private val helperService: HelperService
) : IncomeService {
    override fun incomes(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.INCOMES)
        val message = SendMessage(chatId, ButtonPressed.INCOMES.text)
        message.replyMarkup = inlineKeyboardService.incomes(incomeApiClient.getTodayIncomes(UUID.randomUUID()))
        return message
    }

    override fun incomesPage(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.INCOMES)
        val message = SendMessage(chatId, ButtonPressed.INCOMES.text)
        message.replyMarkup =
            inlineKeyboardService.incomes(
                incomeApiClient.getTodayIncomes(
                    UUID.randomUUID(),
                    helperService.getPageNumber(data)
                )
            )
        return message
    }

    override fun income(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.INCOMES)
        val id = helperService.getId(data)
        val message = SendMessage(chatId, ButtonPressed.INCOMES.text)
        message.replyMarkup = inlineKeyboardService.income(incomeApiClient.getIncome(id))
        return message
    }

    override fun incomeAdd(chatId: String, data: String): SendMessage? {
        stateCacheService.cache(chatId, State.INCOME_ADD)
        val cached = stateCacheService.getIncomeAdd(chatId)
        return if (cached == null) {
            stateCacheService.cacheIncomeAdd(chatId)
            val message = SendMessage(chatId, "Choose the category of new income:")
            message.replyMarkup = inlineKeyboardService.chooseIncomeCategory(
                incomeApiClient.getIncomeCategories(chatId),
                Query.ADD_INCOME
            )
            message
        } else if (data.contains(QueryParts.INCOME_CATEGORY.path)) {
            stateCacheService.cacheIncomeAdd(chatId, getCategoryId(data))
            SendMessage(chatId, "Enter the name of new income:")
        } else {
            null
        }
    }

    override fun incomeEdit(chatId: String, data: String): SendMessage? {
        stateCacheService.cache(chatId, State.INCOME_EDIT)
        val cached = stateCacheService.getIncomeEdit(chatId)
        return if (cached == null) {
            stateCacheService.cacheIncomeEdit(chatId, id = helperService.getId(data), categoryId = null)
            val message = SendMessage(chatId, "Choose the category of income:")
            message.replyMarkup = inlineKeyboardService.chooseIncomeCategory(
                incomeApiClient.getIncomeCategories(chatId),
                Query.EDIT_INCOME
            )
            message
        } else if (data.contains(QueryParts.INCOME_CATEGORY.path)) {
            stateCacheService.cacheIncomeEdit(chatId, categoryId = getCategoryId(data), id = null)
            SendMessage(chatId, "Enter new name of income:")
        } else {
            null
        }
    }

    override fun incomeDelete(chatId: String, data: String): SendMessage {
        incomeApiClient.deleteIncome(chatId, helperService.getId(data))
        stateCacheService.cache(chatId, State.INCOMES)
        return incomes(chatId)
    }

    private fun getCategoryId(data: String): UUID {
        return UUID.fromString(data.substringAfter(QueryParts.INCOME_CATEGORY.path))
    }
}