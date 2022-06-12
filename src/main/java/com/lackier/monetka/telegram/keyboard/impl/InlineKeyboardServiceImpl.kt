package com.lackier.monetka.telegram.keyboard.impl

import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.QueryParts
import com.lackier.monetka.telegram.external.dto.Group
import com.lackier.monetka.telegram.external.dto.Transaction
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

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
                listOf(InlineKeyboardButton(ButtonPressed.GROUPS.text, ButtonPressed.GROUPS.path)),
                listOf(InlineKeyboardButton(ButtonPressed.STATISTICS.text, ButtonPressed.STATISTICS.path)),
                listOf(InlineKeyboardButton(ButtonPressed.BACK.text, ButtonPressed.BACK.path))
            )
        return inlineKeyboardMarkup
    }

    override fun groups(groups: Page<Group>): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard =
            groups.content.map { group -> listOf(groupButton(group)) }
        inlineKeyboardMarkup.keyboard += addButton(ButtonPressed.GROUPS)
        inlineKeyboardMarkup.keyboard += paging(groups, ButtonPressed.GROUPS)
        return inlineKeyboardMarkup
    }

    private fun groupButton(group: Group): InlineKeyboardButton {
        return InlineKeyboardButton(
            group.name + " (" + group.type.text + ')',
            ButtonPressed.GROUPS.path + QueryParts.ID_QUERY.path + group.id
        )
    }

    private fun addButton(buttonPressed: ButtonPressed): List<InlineKeyboardButton> {
        return listOf(InlineKeyboardButton(ButtonPressed.ADD.text, buttonPressed.path + ButtonPressed.ADD.path))
    }

    private fun paging(groups: Page<*>, buttonPressed: ButtonPressed): List<InlineKeyboardButton> {
        return when {
            groups.isFirst and groups.isLast -> {
                listOf(
                    InlineKeyboardButton(ButtonPressed.TO_MENU.text, ButtonPressed.TO_MENU.path)
                )
            }
            groups.isFirst -> {
                listOf(
                    InlineKeyboardButton(ButtonPressed.TO_MENU.text, ButtonPressed.TO_MENU.path),
                    nextPageButton(groups.pageable, buttonPressed)
                )
            }
            groups.isLast -> {
                listOf(
                    InlineKeyboardButton(ButtonPressed.TO_MENU.text, ButtonPressed.TO_MENU.path),
                    previousPageButton(groups.pageable, buttonPressed)
                )
            }
            else -> {
                listOf(
                    InlineKeyboardButton(ButtonPressed.TO_MENU.text, ButtonPressed.TO_MENU.path),
                    previousPageButton(groups.pageable, buttonPressed),
                    nextPageButton(groups.pageable, buttonPressed)
                )
            }
        }
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

    override fun chooseGroupType(): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard =
            listOf(
                listOf(
                    InlineKeyboardButton(ButtonPressed.GROUP_INCOME.text, ButtonPressed.GROUP_INCOME.path),
                    InlineKeyboardButton(ButtonPressed.GROUP_EXPENSE.text, ButtonPressed.GROUP_EXPENSE.path)
                )
            )
        return inlineKeyboardMarkup
    }
}