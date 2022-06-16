package com.lackier.monetka.telegram.dto.enum

enum class QueryParts(val path: String) {
    PAGE_QUERY("_page="),
    ID_QUERY("_id="),
    ADD_QUERY("_add"),
    EDIT_QUERY("_edit"),
    DELETE_QUERY("_delete"),
    GROUP_TYPE("_group-type=")
}