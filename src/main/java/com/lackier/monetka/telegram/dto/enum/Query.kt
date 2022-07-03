package com.lackier.monetka.telegram.dto.enum

enum class Query(val path: String) {
    CATEGORY_PAGE(ButtonPressed.CATEGORIES.path + QueryParts.PAGE_QUERY.path),
    CATEGORY(ButtonPressed.CATEGORIES.path + QueryParts.ID_QUERY.path),
    ADD_CATEGORY(ButtonPressed.CATEGORIES.path + QueryParts.ADD_QUERY.path),
    EDIT_CATEGORY(ButtonPressed.CATEGORIES.path + QueryParts.EDIT_QUERY.path),
    DELETE_CATEGORY(ButtonPressed.CATEGORIES.path + QueryParts.DELETE_QUERY.path + QueryParts.ID_QUERY.path),

    INCOME_PAGE(ButtonPressed.INCOMES.path + QueryParts.PAGE_QUERY.path),
    INCOME(ButtonPressed.INCOMES.path + QueryParts.ID_QUERY.path),
    ADD_INCOME(ButtonPressed.INCOMES.path + QueryParts.ADD_QUERY.path),
    EDIT_INCOME(ButtonPressed.INCOMES.path + QueryParts.EDIT_QUERY.path),
    DELETE_INCOME(ButtonPressed.INCOMES.path + QueryParts.DELETE_QUERY.path + QueryParts.ID_QUERY.path),

    EXPENSE_PAGE(ButtonPressed.EXPENSES.path + QueryParts.PAGE_QUERY.path),
    EXPENSE(ButtonPressed.EXPENSES.path + QueryParts.ID_QUERY.path),
    ADD_EXPENSE(ButtonPressed.EXPENSES.path + QueryParts.ADD_QUERY.path),
    EDIT_EXPENSE(ButtonPressed.EXPENSES.path + QueryParts.EDIT_QUERY.path),
    DELETE_EXPENSE(ButtonPressed.EXPENSES.path + QueryParts.DELETE_QUERY.path + QueryParts.ID_QUERY.path),
}