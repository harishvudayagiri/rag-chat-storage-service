
package com.rag.chatbot.service;

import com.rag.chatbot.dto.CreateSessionRequest;
import com.rag.chatbot.exception.ResourceNotFoundException;
import com.rag.chatbot.model.ChatSession;
import com.rag.chatbot.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatSessionServiceTest {

    @Mock
    private ChatSessionRepository repository;

    @InjectMocks
    private ChatSessionService service;

    @Test
    void shouldCreateChatSession() {
        CreateSessionRequest request =
                new CreateSessionRequest("user-123", "My Chat", false);

        ChatSession saved = new ChatSession();
        saved.setId(UUID.randomUUID());
        saved.setUserId("user-123");
        saved.setName("My Chat");
        saved.setFavorite(false);

        when(repository.save(any(ChatSession.class))).thenReturn(saved);

        ChatSession result = service.create(request);

        assertThat(result.getUserId()).isEqualTo("user-123");
        assertThat(result.getName()).isEqualTo("My Chat");
        assertThat(result.isFavorite()).isFalse();

        verify(repository).save(any(ChatSession.class));
    }

    @Test
    void shouldRenameSession() {
        UUID sessionId = UUID.randomUUID();
        ChatSession session = new ChatSession();
        session.setId(sessionId);
        session.setName("Old Name");

        when(repository.findById(sessionId)).thenReturn(Optional.of(session));

        ChatSession renamed = service.renameSession(sessionId, "New Name");

        assertThat(renamed.getName()).isEqualTo("New Name");
    }

    @Test
    void shouldReturnSessionWhenFound() {
        UUID sessionId = UUID.randomUUID();
        ChatSession session = new ChatSession();
        session.setId(sessionId);

        when(repository.findById(sessionId)).thenReturn(Optional.of(session));

        ChatSession result = service.get(sessionId);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFound() {
        UUID sessionId = UUID.randomUUID();

        when(repository.findById(sessionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(sessionId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Chat session not found");
    }
}

