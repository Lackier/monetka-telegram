package com.lackier.monetka.telegram.service.api

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

interface CategoryService {
    fun categories(chatId: String): SendMessage
    fun categoriesPage(chatId: String, data: String): SendMessage
    fun category(chatId: String, data: String): SendMessage
    fun categoryAdd(chatId: String, data: String): SendMessage?
    fun categoryEdit(chatId: String, data: String): SendMessage?
    fun categoryDelete(chatId: String, data: String): SendMessage
}