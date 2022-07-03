package com.lackier.monetka.telegram.keyboard.api

import com.lackier.monetka.backend.api.dto.CategoryDto
import com.lackier.monetka.backend.api.dto.TransactionDto
import com.lackier.monetka.telegram.dto.enum.Query
import org.springframework.data.domain.Page
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup

interface InlineKeyboardService {
    fun menu(): InlineKeyboardMarkup

    fun incomes(incomes: Page<TransactionDto>): InlineKeyboardMarkup
    fun income(incomeDto: TransactionDto): InlineKeyboardMarkup
    fun chooseIncomeCategory(categories: List<CategoryDto>, query: Query): InlineKeyboardMarkup

    fun expenses(expenses: Page<TransactionDto>): InlineKeyboardMarkup
    fun expense(expenseDto: TransactionDto): InlineKeyboardMarkup
    fun chooseExpenseCategory(categories: List<CategoryDto>, query: Query): InlineKeyboardMarkup

    fun categories(categories: Page<CategoryDto>): InlineKeyboardMarkup
    fun category(categoryDto: CategoryDto): InlineKeyboardMarkup
    fun chooseCategoryType(): InlineKeyboardMarkup
    fun chooseCategoryTypeEdit(): InlineKeyboardMarkup

    fun statistics(): InlineKeyboardMarkup
}