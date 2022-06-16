package com.lackier.monetka.telegram.handler

import com.lackier.monetka.backend.api.client.MonetkaApiClient
import com.lackier.monetka.backend.api.dto.CategoryDto
import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
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
    private val apiClient: MonetkaApiClient
) {
    fun answerMessage(message: Message): BotApiMethod<*> {
        val chatId: String = message.chatId.toString()
        val inputText: String = message.text

        return if ((stateCacheService.getState(chatId) == State.CATEGORY_ADD) and stateCacheService.hasCategoryAdd(chatId)) {
            val categoryAdd = stateCacheService.getCategoryAdd(chatId)
            apiClient.createCategory(CategoryDto(null, chatId, inputText, categoryAdd?.type!!, ZonedDateTime.now()))

            stateCacheService.uncacheCategoryAdd(chatId)
            stateCacheService.cache(chatId, State.CATEGORIES)
            val message = SendMessage(chatId, ButtonPressed.CATEGORIES.text)
            message.replyMarkup = inlineKeyboardService.categories(apiClient.getCategories(UUID.randomUUID()))
            message
        } else if ((stateCacheService.getState(chatId) == State.CATEGORY_EDIT) and stateCacheService.hasCategoryEdit(chatId)) {
            val categoryEdit = stateCacheService.getCategoryEdit(chatId)
            apiClient.editCategory(CategoryDto(categoryEdit?.id, chatId, inputText, categoryEdit?.type!!, ZonedDateTime.now()))

            stateCacheService.uncacheCategoryEdit(chatId)
            stateCacheService.cache(chatId, State.CATEGORIES)
            val message = SendMessage(chatId, ButtonPressed.CATEGORIES.text)
            message.replyMarkup = inlineKeyboardService.categories(apiClient.getCategories(UUID.randomUUID()))
            message
        } else {
            SendMessage(chatId, "Menu")
        }
    }
}