package com.maxx.chatbot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;

    //also can be store in DB
    private final List<Message> history = new ArrayList<>(); //Message is framework specific so that it should not depend on LLM

    // system prompt system instruction
    private static final String SYSTEM_PROMPT =  // multiline string text
            """ 
            You are a customer-support executive for our
            Quick commerce app named snippit.
            
            Your job is to identify the customer's main
            problem and urgency. Answer them related to there query.
            
            Use professional language. If user has an issue,
            use words like I understand your frustration,
            I am really sorry for your trouble etc.
            
            Do not answer any other question which is not
            related to Ordering items query, refund query,
            order tracking status query or company policy query.
            """;


    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(String message){

        // USER role
        history.add(new UserMessage(message));

        // SYSTEM + Conversation History
        String output = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .call()
                .content();

        // ASSISTANT role
        history.add(new AssistantMessage(output));
        //context given to LLM model
        return output;
    }

    public void clearHistory() {
        history.clear();
    }

}
