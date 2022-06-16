package com.lackier.monetka.telegram.keyboard.api

import com.lackier.monetka.telegram.external.dto.Group
import com.lackier.monetka.telegram.external.dto.Transaction
import org.springframework.data.domain.Page
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup

interface InlineKeyboardService {
    fun menu(): InlineKeyboardMarkup
    fun groups(groups: Page<Group>): InlineKeyboardMarkup
    fun incomes(incomes: Page<Transaction>): InlineKeyboardMarkup
    fun expenses(expenses: Page<Transaction>): InlineKeyboardMarkup
    fun statistics(): InlineKeyboardMarkup
    fun chooseGroupType(): InlineKeyboardMarkup
    fun chooseGroupTypeEdit(): InlineKeyboardMarkup
    fun group(group: Group): InlineKeyboardMarkup
}