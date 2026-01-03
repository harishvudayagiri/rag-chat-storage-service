package com.rag.chatbot.dto;

public record CreateSessionRequest(
        String userId,
        String name,
        boolean favorite
) {
}
