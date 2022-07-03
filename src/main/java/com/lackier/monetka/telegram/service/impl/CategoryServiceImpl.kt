package com.lackier.monetka.telegram.service.impl

import com.lackier.monetka.backend.api.client.CategoryApiClient
import com.lackier.monetka.backend.api.enums.CategoryTypeDto
import com.lackier.monetka.telegram.dto.enum.ButtonPressed
import com.lackier.monetka.telegram.dto.enum.QueryParts
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.keyboard.api.InlineKeyboardService
import com.lackier.monetka.telegram.service.api.CategoryService
import com.lackier.monetka.telegram.service.api.HelperService
import com.lackier.monetka.telegram.service.api.StateCacheService
import lombok.AccessLevel
import lombok.experimental.FieldDefaults
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class CategoryServiceImpl(
    private val stateCacheService: StateCacheService,
    private val categoryApiClient: CategoryApiClient,
    private val inlineKeyboardService: InlineKeyboardService,
    private val helperService: HelperService
) : CategoryService {
    override fun categories(chatId: String): SendMessage {
        stateCacheService.cache(chatId, State.CATEGORIES)
        val message = SendMessage(chatId, ButtonPressed.CATEGORIES.text)
        message.replyMarkup = inlineKeyboardService.categories(categoryApiClient.getCategories(UUID.randomUUID()))
        return message
    }

    override fun categoriesPage(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.CATEGORIES)
        val message = SendMessage(chatId, ButtonPressed.CATEGORIES.text)
        message.replyMarkup =
            inlineKeyboardService.categories(
                categoryApiClient.getCategories(
                    UUID.randomUUID(),
                    helperService.getPageNumber(data)
                )
            )
        return message
    }

    override fun category(chatId: String, data: String): SendMessage {
        stateCacheService.cache(chatId, State.CATEGORIES)
        val id = helperService.getId(data)
        val message = SendMessage(chatId, ButtonPressed.CATEGORIES.text)
        message.replyMarkup = inlineKeyboardService.category(categoryApiClient.getCategory(id))
        return message
    }

    override fun categoryAdd(chatId: String, data: String): SendMessage? {
        stateCacheService.cache(chatId, State.CATEGORY_ADD)
        val cached = stateCacheService.getCategoryAdd(chatId)
        return if (cached == null) {
            stateCacheService.cacheCategoryAdd(chatId, null, null)
            val message = SendMessage(chatId, "Choose the type of new category:")
            message.replyMarkup = inlineKeyboardService.chooseCategoryType()
            message
        } else if (data.contains(QueryParts.CATEGORY_TYPE.path)) {
            stateCacheService.cacheCategoryAdd(chatId, getCategoryType(data), null)
            SendMessage(chatId, "Enter the name of new category:")
        } else {
            null
        }
    }

    override fun categoryEdit(chatId: String, data: String): SendMessage? {
        stateCacheService.cache(chatId, State.CATEGORY_EDIT)
        val cached = stateCacheService.getCategoryEdit(chatId)
        return if (cached == null) {
            stateCacheService.cacheCategoryEdit(chatId, helperService.getId(data))
            val message = SendMessage(chatId, "Choose the type of category:")
            message.replyMarkup = inlineKeyboardService.chooseCategoryTypeEdit()
            message
        } else if (data.contains(QueryParts.CATEGORY_TYPE.path)) {
            stateCacheService.cacheCategoryEdit(chatId, getCategoryType(data))
            SendMessage(chatId, "Enter new name of category:")
        } else {
            null
        }
    }

    override fun categoryDelete(chatId: String, data: String): SendMessage {
        categoryApiClient.deleteCategory(chatId, helperService.getId(data))
        stateCacheService.cache(chatId, State.CATEGORIES)
        return categories(chatId)
    }

    private fun getCategoryType(data: String): CategoryTypeDto {
        return CategoryTypeDto.valueOf(data.substringAfter(QueryParts.CATEGORY_TYPE.path))
    }
}