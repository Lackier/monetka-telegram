package com.lackier.monetka.telegram.dto

import com.lackier.monetka.telegram.external.dto.enum.CategoryType
import java.util.*

class CategoryEdit(val date: Date, val id: UUID, var type: CategoryType?, var name: String?)
