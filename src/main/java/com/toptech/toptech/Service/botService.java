package com.toptech.toptech.Service;

import com.toptech.toptech.Service.NewsService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class botService extends TelegramLongPollingBot {

    private final String botUsername;
    private final NewsService newsService;

    public botService(@Value("${telegram.bot.token}") String token,
                      @Value("${telegram.bot.username}") String username,
                      NewsService newsService) {
        super(token);
        this.botUsername = username;
        this.newsService = newsService;
//        System.out.println("Bot started: " + botUsername);
    }

    @Override
    public void clearWebhook() {

    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText().trim().toLowerCase();

        String response;

        switch (text) {
            case "/start" -> response = """
                    👋 Welcome to *TechNewsBot*!
                    
                    Commands:
                    🔹 /fetch — Get top 10 Hacker News stories
                    🔹 /help — Show this menu
                    """;

            case "/fetch", "fetch" -> {
                sendText(chatId, "⏳ Fetching latest tech news...");
                response = newsService.getTop10News();
            }

            case "/help" -> response = """
                    🤖 *TechNewsBot Help*
                    
                    /fetch — Top 10 trending tech stories
                    /start — Welcome message
                    """;

            default -> response = "❓ Unknown command. Type /help to see available commands.";
        }

        sendText(chatId, response);
    }

    private void sendText(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("Markdown");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}