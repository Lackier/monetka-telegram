package com.lackier.monetka.telegram.keyboard.api

import com.lackier.monetka.telegram.external.dto.Category
import com.lackier.monetka.telegram.external.dto.Transaction
import org.springframework.data.domain.Page
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup

interface InlineKeyboardService {
    fun menu(): InlineKeyboardMarkup
    fun categories(categories: Page<Category>): InlineKeyboardMarkup
    fun incomes(incomes: Page<Transaction>): InlineKeyboardMarkup
    fun expenses(expenses: Page<Transaction>): InlineKeyboardMarkup
    fun statistics(): InlineKeyboardMarkup
    fun chooseCategoryType(): InlineKeyboardMarkup
    fun chooseCategoryTypeEdit(): InlineKeyboardMarkup
    fun category(category: Category): InlineKeyboardMarkup
}