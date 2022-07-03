package com.lackier.monetka.telegram.service.api

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

interface ExpenseService {
    fun expenses(chatId: String): SendMessage
    fun expensesPage(chatId: String, data: String): SendMessage
    fun expense(chatId: String, data: String): SendMessage
    fun expenseAdd(chatId: String, data: String): SendMessage?
    fun expenseEdit(chatId: String, data: String): SendMessage?
    fun expenseDelete(chatId: String, data: String): SendMessage
}