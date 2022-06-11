package com.lackier.monetka.telegram.dto

import com.lackier.monetka.telegram.dto.enum.State
import java.util.Date

open class ChatState (open val state: State, open val date: Date)