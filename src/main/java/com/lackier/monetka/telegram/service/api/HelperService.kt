package com.lackier.monetka.telegram.service.api

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

interface HelperService {
    fun getPageNumber(data: String): Int
    fun getId(data: String): UUID
    fun menu(chatId: String): SendMessage
}