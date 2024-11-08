package com.example.telegramBot.service;

import com.example.telegramBot.config.BotConfiguration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfiguration configuration;

    public TelegramBot(BotConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getBotUsername() {
        return configuration.getBotName();
    }

    @Override
    public String getBotToken() {
        return configuration.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "Users":
                    handleUsers(chatId);
                    break;
                case "Reports":
                    handleReports(chatId);
                    break;
                case "Reminders":
                    handleReminders(chatId);
                    break;
                case "Start":
                    handleStart(chatId);
                    break;
                case "Info":
                    handleInfo(chatId);
                    break;
                case "Send Report":
                    handleSendReport(chatId);
                    break;
                default:
                    sendMessage(chatId, "Такой кнопки нет. Слепой?");
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMessage(chatId, answer);

        String role = getUserRole(chatId);
        if ("ADMIN".equals(role)) {
            sendAdminMenu(chatId);
        } else {
            sendUserMenu(chatId);
        }
    }

    private String getUserRole(Long chatId) {
        return chatId == 000000L ? "ADMIN" : "USER";
    }

    private void sendAdminMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Admin Menu:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Users"));
        row1.add(new KeyboardButton("Reports"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Reminders"));

        keyboardMarkup.setKeyboard(List.of(row1, row2));
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendUserMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("User Menu:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Start"));
        row1.add(new KeyboardButton("Info"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Send Report"));

        keyboardMarkup.setKeyboard(List.of(row1, row2));
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Методы-обработчики
    private void handleUsers(long chatId) {
        sendMessage(chatId, "Here is the list of users.");
        // Логика
    }

    private void handleReports(long chatId) {
        sendMessage(chatId, "Here are the reports.");
    }

    private void handleReminders(long chatId) {
        sendMessage(chatId, "Here are the reminders.");
    }

    private void handleStart(long chatId) {
        sendMessage(chatId, "Welcome! How can I assist you?");
    }

    private void handleInfo(long chatId) {
        sendMessage(chatId, "This bot provides various services. Please select an option.");

    }

    private void handleSendReport(long chatId) {
        sendMessage(chatId, "Please type your report and send it here.");

    }

    private void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
