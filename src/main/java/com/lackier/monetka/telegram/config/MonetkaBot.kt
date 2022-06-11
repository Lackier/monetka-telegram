package com.lackier.monetka.telegram.config

import com.lackier.monetka.telegram.handler.CallbackQueryHandler
import com.lackier.monetka.telegram.handler.MessageHandler
import lombok.AccessLevel
import lombok.Data
import lombok.experimental.FieldDefaults
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.starter.SpringWebhookBot
import java.io.IOException

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class MonetkaBot(
    setWebhook: SetWebhook,
    private val messageHandler: MessageHandler,
    private val callbackQueryHandler: CallbackQueryHandler,
    private val telegramConfig: MonetkaBotConfiguration
) : SpringWebhookBot(setWebhook) {

    override fun getBotToken(): String {
        return telegramConfig.botToken!!
    }

    override fun getBotUsername(): String {
        return telegramConfig.botUsername!!
    }

    override fun getBotPath(): String {
        return telegramConfig.webHookPath!!
    }

    override fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>? {
        return try {
            handleUpdate(update)
        } catch (e: IllegalArgumentException) {
            SendMessage(update.message.chatId.toString(), "IllegalArgumentException")
        } catch (e: Exception) {
            SendMessage(update.message.chatId.toString(), "Exception")
        }
    }

    @Throws(IOException::class)
    private fun handleUpdate(update: Update): BotApiMethod<*>? {
        return if (update.hasCallbackQuery()) {
            val callbackQuery: CallbackQuery = update.callbackQuery
            callbackQueryHandler.processCallbackQuery(callbackQuery)
        } else {
            messageHandler.answerMessage(update.message)
        }
    }
}