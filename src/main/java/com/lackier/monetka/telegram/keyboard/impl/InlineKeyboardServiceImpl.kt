package com.lackier.monetka.telegram.keyboard.impl

import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.QueryParts
import com.lackier.monetka.telegram.external.dto.Category
import com.lackier.monetka.telegram.external.dto.Transaction
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.util.*

@Service
class InlineKeyboardServiceImpl : InlineKeyboardService {
    private fun InlineKeyboardButton(text: String, callbackData: String): InlineKeyboardButton {
        return InlineKeyboardButton.builder()
            .text(text)
            .callbackData(callbackData)
            .build()
    }

    override fun menu(): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard =
            listOf(
                listOf(InlineKeyboardButton(ButtonPressed.SETTINGS.text, ButtonPressed.SETTINGS.path)),
                listOf(InlineKeyboardButton(ButtonPressed.INCOMES.text, ButtonPressed.INCOMES.path)),
                listOf(InlineKeyboardButton(ButtonPressed.EXPENSES.text, ButtonPressed.EXPENSES.path)),
                listOf(InlineKeyboardButton(ButtonPressed.CATEGORIES.text, ButtonPressed.CATEGORIES.path)),
                listOf(InlineKeyboardButton(ButtonPressed.STATISTICS.text, ButtonPressed.STATISTICS.path)),
                listOf(InlineKeyboardButton(ButtonPressed.BACK.text, ButtonPressed.BACK.path))
            )
        return inlineKeyboardMarkup
    }

    override fun categories(categories: Page<Category>): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard =
            categories.content.map { category -> listOf(categoryButton(category)) }
        inlineKeyboardMarkup.keyboard += addButton(ButtonPressed.CATEGORIES)
        inlineKeyboardMarkup.keyboard += paging(categories, ButtonPressed.CATEGORIES)
        return inlineKeyboardMarkup
    }

    private fun categoryButton(category: Category): InlineKeyboardButton {
        return InlineKeyboardButton(
            category.name + " (" + category.type.text + ')',
            ButtonPressed.CATEGORIES.path + QueryParts.ID_QUERY.path + category.id
        )
    }

    private fun addButton(buttonPressed: ButtonPressed): List<InlineKeyboardButton> {
        return listOf(InlineKeyboardButton(ButtonPressed.ADD.text, buttonPressed.path + ButtonPressed.ADD.path))
    }

    private fun paging(categories: Page<*>, buttonPressed: ButtonPressed): List<InlineKeyboardButton> {
        return when {
            categories.isFirst and categories.isLast -> {
                listOf(toMenu())
            }
            categories.isFirst -> {
                listOf(toMenu(), nextPageButton(categories.pageable, buttonPressed))
            }
            categories.isLast -> {
                listOf(toMenu(), previousPageButton(categories.pageable, buttonPressed))
            }
            else -> {
                listOf(
                    toMenu(),
                    previousPageButton(categories.pageable, buttonPressed),
                    nextPageButton(categories.pageable, buttonPressed)
                )
            }
        }
    }

    private fun toMenu(): InlineKeyboardButton {
        return InlineKeyboardButton(ButtonPressed.TO_MENU.text, ButtonPressed.TO_MENU.path)
    }

    private fun previousPageButton(pageable: Pageable, buttonPressed: ButtonPressed): InlineKeyboardButton {
        return InlineKeyboardButton("<", buttonPressed.path + QueryParts.PAGE_QUERY.path + (pageable.pageNumber - 1))
    }

    private fun nextPageButton(pageable: Pageable, buttonPressed: ButtonPressed): InlineKeyboardButton {
        return InlineKeyboardButton(">", buttonPressed.path + QueryParts.PAGE_QUERY.path + (pageable.pageNumber + 1))
    }

    override fun incomes(incomes: Page<Transaction>): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard =
            incomes.content.map { transaction -> listOf(transactionButton(transaction, ButtonPressed.INCOMES)) }
        inlineKeyboardMarkup.keyboard += addButton(ButtonPressed.INCOMES)
        inlineKeyboardMarkup.keyboard += paging(incomes, ButtonPressed.INCOMES)
        return inlineKeyboardMarkup
    }

    override fun expenses(expenses: Page<Transaction>): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard =
            expenses.content.map { transaction -> listOf(transactionButton(transaction, ButtonPressed.EXPENSES)) }
        inlineKeyboardMarkup.keyboard += addButton(ButtonPressed.EXPENSES)
        inlineKeyboardMarkup.keyboard += paging(expenses, ButtonPressed.EXPENSES)
        return inlineKeyboardMarkup
    }

    private fun transactionButton(transaction: Transaction, buttonPressed: ButtonPressed): InlineKeyboardButton {
        return InlineKeyboardButton(
            transaction.name + ' ' + transaction.createdAt.hour + ':' + transaction.createdAt.minute,
            buttonPressed.path + QueryParts.ID_QUERY.path + transaction.id
        )
    }

    override fun statistics(): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard =
            listOf(listOf(InlineKeyboardButton(ButtonPressed.TO_MENU.text, ButtonPressed.TO_MENU.path)))
        return inlineKeyboardMarkup
    }

    override fun chooseCategoryType(): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard =
            listOf(
                listOf(
                    InlineKeyboardButton(
                        ButtonPressed.ADD_CATEGORY_INCOME.text,
                        ButtonPressed.ADD_CATEGORY_INCOME.path
                    ),
                    InlineKeyboardButton(
                        ButtonPressed.ADD_CATEGORY_EXPENSE.text,
                        ButtonPressed.ADD_CATEGORY_EXPENSE.path
                    )
                )
            )
        return inlineKeyboardMarkup
    }

    override fun chooseCategoryTypeEdit(): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard =
            listOf(
                listOf(
                    InlineKeyboardButton(
                        ButtonPressed.INCOME.text,
                        ButtonPressed.CATEGORIES.path + ButtonPressed.EDIT.path + ButtonPressed.INCOME.path
                    ),
                    InlineKeyboardButton(
                        ButtonPressed.EXPENSE.text,
                        ButtonPressed.CATEGORIES.path + ButtonPressed.EDIT.path + ButtonPressed.EXPENSE.path
                    )
                )
            )
        return inlineKeyboardMarkup
    }

    override fun category(category: Category): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard = listOf(
            listOf(categoryButton(category)),
            editDeleteButton(category.id!!, ButtonPressed.CATEGORIES),
            listOf(toMenu())
        )
        return inlineKeyboardMarkup
    }

    private fun editDeleteButton(id: UUID, buttonPressed: ButtonPressed): List<InlineKeyboardButton> {
        return listOf(
            InlineKeyboardButton(
                ButtonPressed.EDIT.text,
                buttonPressed.path + ButtonPressed.EDIT.path + QueryParts.ID_QUERY.path + id
            ),
            InlineKeyboardButton(
                ButtonPressed.DELETE.text,
                buttonPressed.path + ButtonPressed.DELETE.path + QueryParts.ID_QUERY.path + id
            )
        )
    }
}