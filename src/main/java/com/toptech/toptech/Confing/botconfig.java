package com.toptech.toptech.Confing;

import com.toptech.toptech.Service.botService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class botconfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(botService botService) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(botService);
            return api;
        } catch (Exception e) {
            System.out.println("Webhook error ignored: " + e.getMessage());
            return null;
        }
    }
}