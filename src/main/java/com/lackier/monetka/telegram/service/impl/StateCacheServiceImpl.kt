package com.lackier.monetka.telegram.service.impl

import com.lackier.monetka.backend.api.enums.CategoryTypeDto
import com.lackier.monetka.telegram.dto.*
import com.lackier.monetka.telegram.dto.enum.State
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
    private val incomeAddCache: Hashtable<String, IncomeAdd> = Hashtable()
    private val incomeEditCache: Hashtable<String, IncomeEdit> = Hashtable()
    private val expenseAddCache: Hashtable<String, ExpenseAdd> = Hashtable()
    private val expenseEditCache: Hashtable<String, ExpenseEdit> = Hashtable()

    override fun cache(chatId: String, state: State) {
        stateCache[chatId] = ChatState(state, Date())
    }

    override fun getState(chatId: String): State {
        val state = stateCache[chatId]
        return state?.state ?: State.DEFAULT
    }

    override fun cacheCategoryAdd(chatId: String, type: CategoryTypeDto?, name: String?) {
        categoryAddCache[chatId] = CategoryAdd(Date(), type, name)
    }

    override fun hasCategoryAdd(chatId: String): Boolean {
        return categoryAddCache.contains(key = chatId)
    }

    override fun getCategoryAdd(chatId: String): CategoryAdd? {
        return categoryAddCache[chatId]
    }

    override fun unCacheCategoryAdd(chatId: String) {
        categoryAddCache.remove(chatId)
    }

    override fun cacheCategoryEdit(chatId: String, id: UUID) {
        categoryEditCache[chatId] = CategoryEdit(Date(), id, null, null)
    }

    override fun cacheCategoryEdit(chatId: String, type: CategoryTypeDto) {
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

    override fun unCacheCategoryEdit(chatId: String) {
        categoryEditCache.remove(chatId)
    }

    override fun getIncomeAdd(chatId: String): IncomeAdd? {
        return incomeAddCache[chatId]
    }

    override fun unCacheIncomeAdd(chatId: String) {
        incomeAddCache.remove(chatId)
    }

    override fun cacheIncomeEdit(chatId: String, id: UUID?, categoryId: UUID?) {
        if (id != null) {
            incomeEditCache[chatId] = IncomeEdit(Date(), id, null, null, null)
        } else {
            val incomeEdit = getIncomeEdit(chatId)
            incomeEdit?.category = categoryId
            incomeEditCache[chatId] = incomeEdit
        }
    }

    override fun cacheIncomeEdit(chatId: String, incomeEditDto: IncomeEdit) {
        val incomeEdit = getIncomeEdit(chatId)
        incomeEdit?.name = incomeEditDto.name
        incomeEdit?.value = incomeEditDto.value
        incomeEditCache[chatId] = incomeEdit
    }

    override fun hasIncomeEdit(chatId: String): Boolean {
        return incomeEditCache.contains(key = chatId)
    }

    override fun cacheIncomeAdd(chatId: String) {
        incomeAddCache[chatId] = IncomeAdd(Date(), null, null, null)
    }

    override fun cacheIncomeAdd(chatId: String, categoryId: UUID) {
        incomeAddCache[chatId] = IncomeAdd(Date(), categoryId, null, null)
    }

    override fun cacheIncomeAdd(chatId: String, incomeAdd: IncomeAdd) {
        incomeAddCache[chatId] = IncomeAdd(Date(), incomeAdd.category, incomeAdd.name, incomeAdd.value)
    }

    override fun hasIncomeAdd(chatId: String): Boolean {
        return incomeAddCache.contains(key = chatId)
    }

    override fun getIncomeEdit(chatId: String): IncomeEdit? {
        return incomeEditCache[chatId]
    }

    override fun unCacheIncomeEdit(chatId: String) {
        incomeEditCache.remove(chatId)
    }

    override fun cacheExpenseAdd(chatId: String) {
        expenseAddCache[chatId] = ExpenseAdd(Date(), null, null, null)
    }

    override fun cacheExpenseAdd(chatId: String, categoryId: UUID) {
        expenseAddCache[chatId] = ExpenseAdd(Date(), categoryId, null, null)
    }

    override fun cacheExpenseAdd(chatId: String, expenseAddDto: ExpenseAdd) {
        val expenseAdd = getExpenseAdd(chatId)
        expenseAdd?.name = expenseAddDto.name
        expenseAdd?.value = expenseAddDto.value
        expenseAddCache[chatId] = expenseAdd
    }

    override fun hasExpenseAdd(chatId: String): Boolean {
        return expenseAddCache.contains(key = chatId)
    }

    override fun getExpenseAdd(chatId: String): ExpenseAdd? {
        return expenseAddCache[chatId]
    }

    override fun unCacheExpenseAdd(chatId: String) {
        expenseAddCache.remove(chatId)
    }

    override fun cacheExpenseEdit(chatId: String, id: UUID?, categoryId: UUID?) {
        if (id != null) {
            expenseEditCache[chatId] = ExpenseEdit(Date(), id, null, null, null)
        } else {
            val expenseEdit = getExpenseEdit(chatId)
            expenseEdit?.category = categoryId
            expenseEditCache[chatId] = expenseEdit
        }
    }

    override fun cacheExpenseEdit(chatId: String, expenseEditDto: ExpenseEdit) {
        val expenseEdit = getExpenseEdit(chatId)
        expenseEdit?.name = expenseEditDto.name
        expenseEdit?.value = expenseEditDto.value
        expenseEditCache[chatId] = expenseEdit
    }

    override fun hasExpenseEdit(chatId: String): Boolean {
        return expenseEditCache.contains(key = chatId)
    }

    override fun getExpenseEdit(chatId: String): ExpenseEdit? {
        return expenseEditCache[chatId]
    }

    override fun unCacheExpenseEdit(chatId: String) {
        expenseEditCache.remove(chatId)
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

    @Scheduled(fixedDelay = 900000, initialDelay = 900000)
    fun clearIncomeAddCache() {
        val date = Date()
        date.hours--
        incomeAddCache.values.removeIf { incomeAdd -> incomeAdd.date.before(date) }
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 900000)
    fun clearIncomeEditCache() {
        val date = Date()
        date.hours--
        incomeEditCache.values.removeIf { incomeEdit -> incomeEdit.date.before(date) }
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 900000)
    fun clearExpenseAddCache() {
        val date = Date()
        date.hours--
        expenseAddCache.values.removeIf { expenseAdd -> expenseAdd.date.before(date) }
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 900000)
    fun clearExpenseEditCache() {
        val date = Date()
        date.hours--
        expenseEditCache.values.removeIf { expenseEdit -> expenseEdit.date.before(date) }
    }
}