package com.lackier.monetka.telegram.dto

import java.util.*

class ExpenseEdit(val date: Date, val id: UUID, var category: UUID?, var name: String?, var value: Double?)