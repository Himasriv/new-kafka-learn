package com.example.ai;

import com.example.ai.model.MovieRecommendation;
import com.example.ai.service.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AiServiceTest {

    @Autowired
    private AiService aiService;

    @TestConfiguration
    static class MockChatModelConfig {

        @Bean
        public ChatModel chatModel() {
            ChatModel mockModel = mock(ChatModel.class);

            ChatResponse mockResponse = new ChatResponse(
                    List.of(new Generation(new AssistantMessage("Hello! I am a helpful AI assistant.")))
            );

            when(mockModel.call(any(Prompt.class))).thenReturn(mockResponse);

            return mockModel;
        }

        @Bean
        public ChatClient.Builder chatClientBuilder(ChatModel chatModel) {
            return ChatClient.builder(chatModel);
        }
    }

    @Test
    void chatReturnsNonEmptyResponse() {
        String response = aiService.chat("Hello, who are you?");
        assertThat(response).isNotBlank();
    }

    @Test
    void chatWithSystemReturnsNonEmptyResponse() {
        String response = aiService.chatWithSystem(
                "Tell me a joke",
                "You are a funny assistant who only tells dad jokes."
        );
        assertThat(response).isNotBlank();
    }
}

