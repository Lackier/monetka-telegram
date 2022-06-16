package com.lackier.monetka.telegram.dto

import com.lackier.monetka.backend.api.enums.CategoryTypeDto
import java.util.*

class CategoryEdit(val date: Date, val id: UUID, var type: CategoryTypeDto?, var name: String?)
