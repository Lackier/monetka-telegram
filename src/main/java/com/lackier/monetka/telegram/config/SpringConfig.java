package com.lackier.monetka.telegram.config;

import com.lackier.monetka.telegram.handler.CallbackQueryHandler;
import com.lackier.monetka.telegram.handler.MessageHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import static java.util.Objects.requireNonNull;

@Configuration
@AllArgsConstructor
public class SpringConfig {
    private final MonetkaBotConfiguration telegramConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(requireNonNull(telegramConfig.getWebHookPath())).build();
    }

    @Bean
    public MonetkaBot springWebhookBot(SetWebhook setWebhook,
                                       MessageHandler messageHandler,
                                       CallbackQueryHandler callbackQueryHandler) {
        return new MonetkaBot(setWebhook, messageHandler, callbackQueryHandler, telegramConfig);
    }
}
