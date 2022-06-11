package com.lackier.monetka.telegram.keyboard.impl

import com.lackier.monetka.telegram.keyboard.api.ReplyKeyboardService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.ArrayList

@Service
class ReplyKeyboardServiceImpl : ReplyKeyboardService {
    override fun defaultReplyKeyboard(): ReplyKeyboardMarkup {
        return replyKeyboard(listOf("Menu", "Back"))
    }

    private fun replyKeyboard(buttons: List<String>): ReplyKeyboardMarkup {
        val row = KeyboardRow()
        buttons.forEach { button -> row.add(button) }

        val keyboard: MutableList<KeyboardRow> = ArrayList()
        keyboard.add(row)

        val keyboardMarkup = ReplyKeyboardMarkup()
        keyboardMarkup.resizeKeyboard = true
        keyboardMarkup.keyboard = keyboard
        return keyboardMarkup
    }
}