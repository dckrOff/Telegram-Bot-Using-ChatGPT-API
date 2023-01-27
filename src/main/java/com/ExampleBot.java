package com;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import okhttp3.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExampleBot extends TelegramLongPollingBot {


    private final String BOT_USERNAME = "BOT_USER_NAME";
    private final String BOT_TOKEN = "BOT_TOKEN";
    String OPEN_AI_KEY = "OPEN_AI_TOKEN";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                System.out.println("message: " + text);
                System.out.println("from: " + message.getChat().getFirstName() + ", @" + message.getChat().getUserName());
                if (text.equalsIgnoreCase("/start")) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("Welcome to my first ai bot (This is not my service, this is ChatGPT). Let me know if you have any other questions.");
                    try {
                        execute(sendMessage);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText(gpt(text));
                    try {
                        execute(sendMessage);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    private String gpt(String text) {
        OpenAiService service = new OpenAiService(OPEN_AI_KEY);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(text)
                .model("text-davinci-003")
                .maxTokens(2048)
                .echo(true)
                .build();

        StringBuilder msg = new StringBuilder();

        List<CompletionChoice> message = service.createCompletion(completionRequest).getChoices();
        for (CompletionChoice completionChoice : message) {
            msg.append(completionChoice.getText());
        }

        return msg.toString();
    }
}
