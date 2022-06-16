package com.lackier.monetka.telegram.keyboard.api

import com.lackier.monetka.backend.api.dto.CategoryDto
import com.lackier.monetka.backend.api.dto.TransactionDto
import org.springframework.data.domain.Page
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup

interface InlineKeyboardService {
    fun menu(): InlineKeyboardMarkup
    fun categories(categories: Page<CategoryDto>): InlineKeyboardMarkup
    fun incomes(incomes: Page<TransactionDto>): InlineKeyboardMarkup
    fun expenses(expenses: Page<TransactionDto>): InlineKeyboardMarkup
    fun statistics(): InlineKeyboardMarkup
    fun chooseCategoryType(): InlineKeyboardMarkup
    fun chooseCategoryTypeEdit(): InlineKeyboardMarkup
    fun category(categoryDto: CategoryDto): InlineKeyboardMarkup
}