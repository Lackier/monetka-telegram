package com.lackier.monetka.telegram.service.impl

import com.lackier.monetka.telegram.dto.ChatState
import com.lackier.monetka.telegram.dto.GroupAdd
import com.lackier.monetka.telegram.dto.GroupEdit
import com.lackier.monetka.telegram.dto.enum.State
import com.lackier.monetka.telegram.external.dto.enum.GroupType
import com.lackier.monetka.telegram.service.api.StateCacheService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*


@Service
@EnableScheduling
class StateCacheServiceImpl : StateCacheService {
    private val stateCache: Hashtable<String, ChatState> = Hashtable()
    private val groupAddCache: Hashtable<String, GroupAdd> = Hashtable()
    private val groupEditCache: Hashtable<String, GroupEdit> = Hashtable()

    override fun cache(chatId: String, state: State) {
        stateCache[chatId] = ChatState(state, Date())
    }

    override fun getState(chatId: String): State {
        val state = stateCache[chatId]
        return state?.state ?: State.DEFAULT
    }

    override fun cacheGroupAdd(chatId: String, type: GroupType?, name: String?) {
        groupAddCache[chatId] = GroupAdd(Date(), type, name)
    }

    override fun hasGroupAdd(chatId: String): Boolean {
        return groupAddCache.contains(key = chatId)
    }

    override fun getGroupAdd(chatId: String): GroupAdd? {
        return groupAddCache[chatId]
    }

    override fun uncacheGroupAdd(chatId: String) {
        groupAddCache.remove(chatId)
    }

    override fun cacheGroupEdit(chatId: String, id: UUID) {
        groupEditCache[chatId] = GroupEdit(id, null, null)
    }

    override fun cacheGroupEdit(chatId: String, type: GroupType) {
        val groupEdit = getGroupEdit(chatId)
        groupEdit?.type = type
        groupEditCache[chatId] = groupEdit
    }

    override fun cacheGroupEdit(chatId: String, name: String) {
        val groupEdit = getGroupEdit(chatId)
        groupEdit?.name = name
        groupEditCache[chatId] = groupEdit
    }

    override fun hasGroupEdit(chatId: String): Boolean {
        return groupEditCache.contains(key = chatId)
    }

    override fun getGroupEdit(chatId: String): GroupEdit? {
        return groupEditCache[chatId]
    }

    override fun uncacheGroupEdit(chatId: String) {
        groupEditCache.remove(chatId)
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 900000)
    fun clearStateCache() {
        val date = Date()
        date.hours--
        stateCache.values.removeIf { chatState -> chatState.date.before(date) }
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 900000)
    fun clearGroupAddCache() {
        val date = Date()
        date.hours--
        groupAddCache.values.removeIf { groupAdd -> groupAdd.date.before(date) }
    }
}