package com.lackier.monetka.telegram.keyboard.api

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup

interface ReplyKeyboardService {
    fun defaultReplyKeyboard(): ReplyKeyboardMarkup
}
