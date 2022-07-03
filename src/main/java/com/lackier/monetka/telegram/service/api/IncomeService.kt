package com.lackier.monetka.telegram.service.api

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

interface IncomeService {
    fun incomes(chatId: String): SendMessage
    fun incomesPage(chatId: String, data: String): SendMessage
    fun income(chatId: String, data: String): SendMessage
    fun incomeAdd(chatId: String, data: String): SendMessage?
    fun incomeEdit(chatId: String, data: String): SendMessage?
    fun incomeDelete(chatId: String, data: String): SendMessage
}