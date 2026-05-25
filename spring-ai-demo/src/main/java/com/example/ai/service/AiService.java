package com.example.ai.service;

import com.example.ai.model.MovieRecommendation;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Spring AI service showcasing:
 * - Basic chat completions
 * - System prompt customization
 * - Prompt templates with variables
 * - Structured output extraction (AI → Java bean)
 */
@Service
public class AiService {

    private final ChatClient chatClient;

    @Value("classpath:templates/translation-prompt.st")
    private Resource translationPromptResource;

    @Value("classpath:templates/summary-prompt.st")
    private Resource summaryPromptResource;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(
                        "You are a friendly and knowledgeable assistant. " +
                        "Provide clear, concise, and helpful responses.")
                .build();
    }

    /**
     * Feature 1: Simple question → answer using ChatClient.
     */
    public String chat(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

    /**
     * Feature 2: Chat with a custom system prompt.
     * The system prompt changes the AI's personality or role.
     */
    public String chatWithSystem(String userMessage, String systemPrompt) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .call()
                .content();
    }

    /**
     * Feature 3: Prompt template with placeholders.
     * Demonstrates parameterized prompt templates using .st (StringTemplate) files.
     */
    public String translate(String text, String targetLanguage) {
        PromptTemplate promptTemplate = new PromptTemplate(translationPromptResource);

        Prompt prompt = promptTemplate.create(Map.of(
                "text", text,
                "language", targetLanguage
        ));

        ChatResponse response = chatClient.prompt(prompt)
                .call()
                .chatResponse();

        return response.getResult().getOutput().getContent();
    }

    /**
     * Feature 4: Prompt template — text summarization.
     */
    public String summarize(String text, int maxWords) {
        PromptTemplate promptTemplate = new PromptTemplate(summaryPromptResource);

        Prompt prompt = promptTemplate.create(Map.of(
                "text", text,
                "maxWords", maxWords
        ));

        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    /**
     * Feature 5: Structured output extraction.
     * Asks the AI to recommend a movie and maps the response to a Java object.
     */
    public MovieRecommendation recommendMovie(String genre) {
        BeanOutputConverter<MovieRecommendation> outputConverter =
                new BeanOutputConverter<>(MovieRecommendation.class);

        String format = outputConverter.getFormat();

        String userMessage = String.format(
                "Recommend one great %s movie. %s",
                genre,
                format
        );

        String rawResponse = chatClient.prompt()
                .system("You are a movie expert. Always respond with valid JSON only, no extra text.")
                .user(userMessage)
                .call()
                .content();

        return outputConverter.convert(rawResponse);
    }
}

