package com.lackier.monetka.telegram.dto.enum

import com.lackier.monetka.telegram.external.dto.enum.CategoryType

enum class ButtonPressed(val path: String, val text: String) {
    MENU("/menu", "Menu"),
    TO_MENU("/menu", "To menu"),
    BACK("/back", "Back"),
    SETTINGS("/settings", "Settings"),
    CATEGORIES("/categories", "Categories"),
    INCOMES("/incomes", "Incomes"),
    EXPENSES("/expenses", "Expenses"),
    STATISTICS("/statistics", "Statistics"),
    ADD(QueryParts.ADD_QUERY.path, "Add new"),
    ADD_CATEGORY_INCOME(
        CATEGORIES.path + QueryParts.ADD_QUERY.path + QueryParts.CATEGORY_TYPE.path + CategoryType.INCOME,
        "Income"
    ),
    ADD_CATEGORY_EXPENSE(
        CATEGORIES.path + QueryParts.ADD_QUERY.path + QueryParts.CATEGORY_TYPE.path + CategoryType.EXPENSE,
        "Expense"
    ),
    EDIT(QueryParts.EDIT_QUERY.path, "Edit"),
    INCOME(QueryParts.CATEGORY_TYPE.path + CategoryType.INCOME, "Income"),
    EXPENSE(QueryParts.CATEGORY_TYPE.path + CategoryType.EXPENSE, "Expense"),
    DELETE(QueryParts.DELETE_QUERY.path, "Delete")
}