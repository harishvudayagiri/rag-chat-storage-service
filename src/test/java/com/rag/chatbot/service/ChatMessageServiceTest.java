package com.rag.chatbot.service;

import com.rag.chatbot.dto.CreateMessageRequest;
import com.rag.chatbot.model.ChatMessage;
import com.rag.chatbot.model.ChatSession;
import com.rag.chatbot.model.Sender;
import com.rag.chatbot.repository.ChatMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository repository;

    @Mock
    private ChatSessionService sessionService;

    @InjectMocks
    private ChatMessageService service;

    @Test
    void shouldAddMessageToSession() {
        UUID sessionId = UUID.randomUUID();
        ChatSession session = new ChatSession();
        session.setId(sessionId);

        CreateMessageRequest request =
                new CreateMessageRequest(Sender.USER, "Hello", null);

        when(sessionService.get(sessionId)).thenReturn(session);
        when(repository.save(any(ChatMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ChatMessage message = service.addMessage(sessionId, request);

        assertThat(message.getChatSession()).isEqualTo(session);
        assertThat(message.getContent()).isEqualTo("Hello");
        assertThat(message.getSender()).isEqualTo(Sender.USER);
    }

    @Test
    void shouldReturnPagedMessages() {
        UUID sessionId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        ChatMessage msg = new ChatMessage();
        Page<ChatMessage> page = new PageImpl<>(List.of(msg));

        when(repository.findByChatSession_Id(sessionId, pageable))
                .thenReturn(page);

        Page<ChatMessage> result =
                service.getMessages(sessionId, pageable);

        assertThat(result.getContent()).hasSize(1);
    }
}
