package com.lackier.monetka.telegram.controller

import com.lackier.monetka.telegram.config.MonetkaBot
import lombok.AllArgsConstructor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update


@RestController
@AllArgsConstructor
class WebhookController(monetkaBot: MonetkaBot) {
    private val monetkaBot: MonetkaBot

    init {
        this.monetkaBot = monetkaBot
    }

    @PostMapping("/")
    fun onUpdateReceived(@RequestBody update: Update): BotApiMethod<*>? {
        return monetkaBot.onWebhookUpdateReceived(update)
    }
}