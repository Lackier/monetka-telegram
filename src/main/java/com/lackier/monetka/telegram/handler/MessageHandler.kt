package com.lackier.monetka.telegram.handler

import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.external.api.MonetkaApiClient
import com.lackier.monetka.telegram.external.dto.Group
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

        return if ((stateCacheService.getState(chatId) == State.GROUP_ADD) and stateCacheService.hasGroupAdd(chatId)) {
            val groupAdd = stateCacheService.getGroupAdd(chatId)
            apiClient.createGroup(Group(null, chatId, inputText, groupAdd?.type!!, ZonedDateTime.now()))

            stateCacheService.uncacheGroupAdd(chatId)
            stateCacheService.cache(chatId, State.GROUPS)
            val message = SendMessage(chatId, ButtonPressed.GROUPS.text)
            message.replyMarkup = inlineKeyboardService.groups(apiClient.getGroups(UUID.randomUUID()))
            message
        } else if ((stateCacheService.getState(chatId) == State.GROUP_EDIT) and stateCacheService.hasGroupEdit(chatId)) {
            val groupAdd = stateCacheService.getGroupEdit(chatId)
            apiClient.editGroup(Group(groupAdd?.id, chatId, inputText, groupAdd?.type!!, ZonedDateTime.now()))

            stateCacheService.uncacheGroupEdit(chatId)
            stateCacheService.cache(chatId, State.GROUPS)
            val message = SendMessage(chatId, ButtonPressed.GROUPS.text)
            message.replyMarkup = inlineKeyboardService.groups(apiClient.getGroups(UUID.randomUUID()))
            message
        } else {
            SendMessage(chatId, "Menu")
        }
    }
}