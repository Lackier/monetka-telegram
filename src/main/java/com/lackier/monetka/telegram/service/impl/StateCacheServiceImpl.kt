package com.lackier.monetka.telegram.service.impl

import com.lackier.monetka.telegram.dto.ChatState
import com.lackier.monetka.telegram.dto.CategoryAdd
import com.lackier.monetka.telegram.dto.CategoryEdit
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.external.dto.enum.CategoryType
import com.lackier.monetka.telegram.service.api.StateCacheService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*


@Service
@EnableScheduling
class StateCacheServiceImpl : StateCacheService {
    private val stateCache: Hashtable<String, ChatState> = Hashtable()
    private val categoryAddCache: Hashtable<String, CategoryAdd> = Hashtable()
    private val categoryEditCache: Hashtable<String, CategoryEdit> = Hashtable()

    override fun cache(chatId: String, state: State) {
        stateCache[chatId] = ChatState(state, Date())
    }

    override fun getState(chatId: String): State {
        val state = stateCache[chatId]
        return state?.state ?: State.DEFAULT
    }

    override fun cacheCategoryAdd(chatId: String, type: CategoryType?, name: String?) {
        categoryAddCache[chatId] = CategoryAdd(Date(), type, name)
    }

    override fun hasCategoryAdd(chatId: String): Boolean {
        return categoryAddCache.contains(key = chatId)
    }

    override fun getCategoryAdd(chatId: String): CategoryAdd? {
        return categoryAddCache[chatId]
    }

    override fun uncacheCategoryAdd(chatId: String) {
        categoryAddCache.remove(chatId)
    }

    override fun cacheCategoryEdit(chatId: String, id: UUID) {
        categoryEditCache[chatId] = CategoryEdit(Date(), id, null, null)
    }

    override fun cacheCategoryEdit(chatId: String, type: CategoryType) {
        val categoryEdit = getCategoryEdit(chatId)
        categoryEdit?.type = type
        categoryEditCache[chatId] = categoryEdit
    }

    override fun cacheCategoryEdit(chatId: String, name: String) {
        val categoryEdit = getCategoryEdit(chatId)
        categoryEdit?.name = name
        categoryEditCache[chatId] = categoryEdit
    }

    override fun hasCategoryEdit(chatId: String): Boolean {
        return categoryEditCache.contains(key = chatId)
    }

    override fun getCategoryEdit(chatId: String): CategoryEdit? {
        return categoryEditCache[chatId]
    }

    override fun uncacheCategoryEdit(chatId: String) {
        categoryEditCache.remove(chatId)
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 900000)
    fun clearStateCache() {
        val date = Date()
        date.hours--
        stateCache.values.removeIf { chatState -> chatState.date.before(date) }
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 900000)
    fun clearCategoryAddCache() {
        val date = Date()
        date.hours--
        categoryAddCache.values.removeIf { categoryAdd -> categoryAdd.date.before(date) }
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 900000)
    fun clearCategoryEditCache() {
        val date = Date()
        date.hours--
        categoryEditCache.values.removeIf { categoryEdit -> categoryEdit.date.before(date) }
    }
}