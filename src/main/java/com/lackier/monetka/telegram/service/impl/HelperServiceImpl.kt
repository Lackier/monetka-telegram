package com.lackier.monetka.telegram.service.impl

import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.QueryParts
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
import com.lackier.monetka.telegram.service.api.HelperService
import com.lackier.monetka.telegram.service.api.StateCacheService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

@Service
class HelperServiceImpl(
    private val stateCacheService: StateCacheService,
    private val inlineKeyboardService: InlineKeyboardService
) : HelperService {
    override fun getPageNumber(data: String): Int {
        val pageNumber = data.substringAfter(QueryParts.PAGE_QUERY.path)
        return if (pageNumber.isEmpty()) 0 else pageNumber.toInt()
    }

    override fun getId(data: String): UUID {
        return UUID.fromString(data.substringAfter(QueryParts.ID_QUERY.path))
    }

    override fun menu(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.MENU)
        val message = SendMessage(chatId, ButtonPressed.MENU.text)
        message.replyMarkup = inlineKeyboardService.menu()
        return message
    }
}