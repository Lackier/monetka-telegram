package com.lackier.monetka.telegram.dto.enum

import com.lackier.monetka.telegram.external.dto.enum.GroupType

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
    ADD_GROUP_INCOME(GROUPS.path + QueryParts.ADD_QUERY.path + QueryParts.GROUP_TYPE.path + GroupType.INCOME, "Income"),
    ADD_GROUP_EXPENSE(
        GROUPS.path + QueryParts.ADD_QUERY.path + QueryParts.GROUP_TYPE.path + GroupType.EXPENSE,
        "Expense"
    ),
    EDIT(QueryParts.EDIT_QUERY.path, "Edit"),
    INCOME(QueryParts.GROUP_TYPE.path + GroupType.INCOME, "Income"),
    EXPENSE(QueryParts.GROUP_TYPE.path + GroupType.EXPENSE, "Expense"),
    DELETE(QueryParts.DELETE_QUERY.path, "Delete")
}