package com.lackier.monetka.telegram.handler

import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.Query
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
import com.lackier.monetka.telegram.service.api.*
import lombok.AccessLevel
import lombok.experimental.FieldDefaults
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class CallbackQueryHandler(
    private val inlineKeyboardService: InlineKeyboardService,
    private val stateCacheService: StateCacheService,
    private val categoryService: CategoryService,
    private val incomeService: IncomeService,
    private val expenseService: ExpenseService,
    private val helperService: HelperService
) {
    fun processCallbackQuery(buttonQuery: CallbackQuery): BotApiMethod<*>? {
        val chatId = buttonQuery.message.chatId.toString()
        val data = buttonQuery.data!!

        return when {
            data == ButtonPressed.MENU.path -> helperService.menu(chatId)
            data == ButtonPressed.SETTINGS.path -> settings(chatId)

            data == ButtonPressed.CATEGORIES.path -> categoryService.categories(chatId)
            data.startsWith(Query.CATEGORY_PAGE.path) -> categoryService.categoriesPage(chatId, data)
            data.startsWith(Query.CATEGORY.path) -> categoryService.category(chatId, data)
            data.startsWith(Query.ADD_CATEGORY.path) -> categoryService.categoryAdd(chatId, data)
            data.startsWith(Query.EDIT_CATEGORY.path) -> categoryService.categoryEdit(chatId, data)
            data.startsWith(Query.DELETE_CATEGORY.path) -> categoryService.categoryDelete(chatId, data)

            data == ButtonPressed.INCOMES.path -> incomeService.incomes(chatId)
            data.startsWith(Query.INCOME_PAGE.path) -> incomeService.incomesPage(chatId, data)
            data.startsWith(Query.INCOME.path) -> incomeService.income(chatId, data)
            data.startsWith(Query.ADD_INCOME.path) -> incomeService.incomeAdd(chatId, data)
            data.startsWith(Query.EDIT_INCOME.path) -> incomeService.incomeEdit(chatId, data)
            data.startsWith(Query.DELETE_INCOME.path) -> incomeService.incomeDelete(chatId, data)

            data == ButtonPressed.EXPENSES.path -> expenseService.expenses(chatId)
            data.startsWith(Query.EXPENSE_PAGE.path) -> expenseService.expensesPage(chatId, data)
            data.startsWith(Query.EXPENSE.path) -> expenseService.expense(chatId, data)
            data.startsWith(Query.ADD_EXPENSE.path) -> expenseService.expenseAdd(chatId, data)
            data.startsWith(Query.EDIT_EXPENSE.path) -> expenseService.expenseEdit(chatId, data)
            data.startsWith(Query.DELETE_EXPENSE.path) -> expenseService.expenseDelete(chatId, data)

            data == ButtonPressed.STATISTICS.path -> statistics(chatId)
            else -> default(chatId)
        }
    }

    private fun default(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.DEFAULT)
        return SendMessage(chatId, "Default")
    }

    private fun settings(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.SETTINGS)
        val message = SendMessage(chatId, ButtonPressed.SETTINGS.text)
        message.replyMarkup = inlineKeyboardService.menu()
        return message
    }

    private fun statistics(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.STATISTICS)
        val message = SendMessage(chatId, ButtonPressed.STATISTICS.text)
        message.replyMarkup = inlineKeyboardService.statistics()//TODO
        return message
    }
}
