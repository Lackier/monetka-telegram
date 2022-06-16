package com.lackier.monetka.telegram.dto

import com.lackier.monetka.telegram.external.dto.enum.GroupType
import java.util.*

class GroupEdit(val id: UUID, var type: GroupType?, var name: String?)
