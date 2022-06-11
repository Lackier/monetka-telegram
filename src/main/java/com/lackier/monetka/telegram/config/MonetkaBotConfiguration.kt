package com.lackier.monetka.telegram.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
open class MonetkaBotConfiguration {
    @Value("\${telegram.bot-token}")
    val botToken: String? = null

    @Value("\${telegram.bot-name}")
    val botUsername: String? = null

    @Value("\${telegram.webhook-path}")
    val webHookPath: String? = null
}