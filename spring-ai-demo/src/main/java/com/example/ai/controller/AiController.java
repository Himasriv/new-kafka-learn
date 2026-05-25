package com.example.ai.controller;

import com.example.ai.model.ChatRequest;
import com.example.ai.model.MovieRecommendation;
import com.example.ai.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller exposing Spring AI features as HTTP endpoints.
 *
 * Endpoints:
 *   POST /ai/chat              - Simple text chat
 *   POST /ai/chat/system       - Chat with custom system prompt
 *   POST /ai/translate         - Translate text to a language
 *   POST /ai/summarize         - Summarize long text
 *   GET  /ai/movie/{genre}     - Get structured movie recommendation
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    /**
     * Feature 1: Simple chat.
     *
     * Request:
     *   POST /ai/chat
     *   {"message": "What is the capital of France?"}
     *
     * Response:
     *   {"reply": "The capital of France is Paris."}
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody ChatRequest request) {
        String reply = aiService.chat(request.getMessage());
        return ResponseEntity.ok(Map.of("reply", reply));
    }

    /**
     * Feature 2: Chat with custom system prompt.
     *
     * Request:
     *   POST /ai/chat/system
     *   {"message": "Tell me a joke", "systemPrompt": "You are a stand-up comedian who only makes dad jokes."}
     */
    @PostMapping("/chat/system")
    public ResponseEntity<Map<String, String>> chatWithSystem(@RequestBody ChatRequest request) {
        String system = request.getSystemPrompt() != null
                ? request.getSystemPrompt()
                : "You are a helpful assistant.";

        String reply = aiService.chatWithSystem(request.getMessage(), system);
        return ResponseEntity.ok(Map.of("reply", reply));
    }

    /**
     * Feature 3: Translate text using a prompt template.
     *
     * Request:
     *   POST /ai/translate
     *   {"text": "Hello, how are you?", "targetLanguage": "Spanish"}
     */
    @PostMapping("/translate")
    public ResponseEntity<Map<String, String>> translate(@RequestBody Map<String, String> body) {
        String text = body.getOrDefault("text", "");
        String targetLanguage = body.getOrDefault("targetLanguage", "Spanish");

        String translation = aiService.translate(text, targetLanguage);
        return ResponseEntity.ok(Map.of("translation", translation));
    }

    /**
     * Feature 4: Summarize text.
     *
     * Request:
     *   POST /ai/summarize
     *   {"text": "Long text here...", "maxWords": 50}
     */
    @PostMapping("/summarize")
    public ResponseEntity<Map<String, String>> summarize(@RequestBody Map<String, Object> body) {
        String text = (String) body.getOrDefault("text", "");
        int maxWords = (int) body.getOrDefault("maxWords", 50);

        String summary = aiService.summarize(text, maxWords);
        return ResponseEntity.ok(Map.of("summary", summary));
    }

    /**
     * Feature 5: Get structured AI output (movie recommendation).
     *
     * Request:
     *   GET /ai/movie/action
     *
     * Response (structured bean):
     *   {"title": "...", "genre": "...", "year": ..., "director": "...", "reason": "..."}
     */
    @GetMapping("/movie/{genre}")
    public ResponseEntity<MovieRecommendation> recommendMovie(@PathVariable String genre) {
        MovieRecommendation recommendation = aiService.recommendMovie(genre);
        return ResponseEntity.ok(recommendation);
    }
}

