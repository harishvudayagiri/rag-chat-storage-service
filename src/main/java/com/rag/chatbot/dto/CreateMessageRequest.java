package com.rag.chatbot.dto;


import com.rag.chatbot.model.Sender;

public record CreateMessageRequest(
        Sender sender,
        String content,
        String retrievedContext
) {

}
