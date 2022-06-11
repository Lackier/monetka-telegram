package com.lackier.monetka.telegram.enum

import com.lackier.monetka.telegram.external.enum.GroupType

enum class ButtonPressed(val path: String, val text: String) {
    MENU("/menu", "Menu"),
    TO_MENU("/menu", "To menu"),
    BACK("/back", "Back"),
    SETTINGS("/settings", "Settings"),
    GROUPS("/groups", "Groups"),
    INCOMES("/incomes", "Incomes"),
    EXPENSES("/expenses", "Expenses"),
    STATISTICS("/statistics", "Statistics"),
    ADD(QueryParts.ADD_QUERY.path, "Add new"),
    GROUP_INCOME(GROUPS.path + QueryParts.ADD_QUERY.path + QueryParts.GROUP_TYPE.path + GroupType.INCOME, "Income"),
    GROUP_EXPENSE(GROUPS.path + QueryParts.ADD_QUERY.path + QueryParts.GROUP_TYPE.path + GroupType.EXPENSE, "Expense")
}