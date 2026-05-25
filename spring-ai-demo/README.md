# Spring AI Demo Project

A practical Spring AI application demonstrating how to build AI-powered Java applications using Spring Boot and OpenAI.

---

## What is Spring AI?

Spring AI is a Spring Framework project for building **AI-powered Spring applications** using familiar patterns.

It provides:
- **`ChatClient`** - High-level, fluent API to talk to any LLM
- **`ChatModel`** - Low-level interface for chat completions
- **`EmbeddingModel`** - Converts text to vectors
- **`VectorStore`** - Semantic / similarity search
- **`PromptTemplate`** - Parameterized, reusable prompts
- **`BeanOutputConverter`** - Maps AI response → Java object
- **RAG** - Retrieval Augmented Generation

### AI providers supported

- OpenAI (ChatGPT, GPT-4o, etc.)
- Azure OpenAI
- Anthropic Claude
- Google Vertex AI / Gemini
- Ollama (local models)
- Amazon Bedrock

---

## What this project demonstrates

| Feature | Endpoint | Description |
|---|---|---|
| Simple chat | `POST /ai/chat` | Basic question and answer |
| System prompt | `POST /ai/chat/system` | Custom AI personality/role |
| Prompt template | `POST /ai/translate` | Parameterized prompt via `.st` file |
| Summarization | `POST /ai/summarize` | Prompt template for text summary |
| Structured output | `GET /ai/movie/{genre}` | AI response maps to a Java bean |

---

## Project structure

```
src/
├── main/
│   ├── java/com/example/ai/
│   │   ├── SpringAiApplication.java     Main entry point
│   │   ├── controller/
│   │   │   └── AiController.java        REST endpoints
│   │   ├── service/
│   │   │   └── AiService.java           AI features using ChatClient
│   │   └── model/
│   │       ├── ChatRequest.java         Request payload
│   │       └── MovieRecommendation.java Structured output model
│   └── resources/
│       ├── application.properties       Config + API key
│       └── templates/
│           ├── translation-prompt.st    Prompt template file
│           └── summary-prompt.st        Prompt template file
└── test/
    └── java/com/example/ai/
        └── AiServiceTest.java           Mock-based tests
```

---

## Setup

### 1. Get an OpenAI API key

Sign up at [https://platform.openai.com](https://platform.openai.com).

### 2. Set your API key

Option A — environment variable (recommended):

```powershell
$env:OPENAI_API_KEY = "sk-your-key-here"
```

Option B — edit `application.properties`:

```properties
spring.ai.openai.api-key=sk-your-key-here
```

---

## Run the application

```powershell
mvn spring-boot:run
```

---

## Run the tests

```powershell
mvn test
```

Tests use a mocked `ChatModel` so no API key is needed.

---

## Try the endpoints

### 1. Simple chat

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/ai/chat" `
  -ContentType "application/json" `
  -Body '{"message": "What is the capital of Japan?"}'
```

### 2. Chat with a custom system prompt (change AI personality)

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/ai/chat/system" `
  -ContentType "application/json" `
  -Body '{"message": "Explain gravity", "systemPrompt": "You are a pirate. Answer everything in pirate speak."}'
```

### 3. Translate text

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/ai/translate" `
  -ContentType "application/json" `
  -Body '{"text": "Hello, how are you?", "targetLanguage": "French"}'
```

### 4. Summarize text

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/ai/summarize" `
  -ContentType "application/json" `
  -Body '{"text": "Spring AI is a framework that offers a consistent API to interact with various AI providers. It borrows concepts from Python libraries such as LangChain but adapts them to the idiomatic Spring-style programming model.", "maxWords": 20}'
```

### 5. Get structured movie recommendation

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/ai/movie/thriller"
```

Expected JSON response:

```json
{
  "title": "Parasite",
  "genre": "Thriller",
  "year": 2019,
  "director": "Bong Joon-ho",
  "reason": "A masterful blend of dark comedy and thriller..."
}
```

---

## Key Spring AI concepts shown in this project

### ChatClient (fluent API)

```java
chatClient.prompt()
    .system("You are a pirate.")
    .user("Tell me about treasure.")
    .call()
    .content();
```

### Prompt templates

```java
PromptTemplate template = new PromptTemplate(resource);
Prompt prompt = template.create(Map.of("text", text, "language", lang));
chatClient.prompt(prompt).call().content();
```

Template file (`translation-prompt.st`):
```
Translate the following text to {language}.
Text: {text}
```

### Structured output (AI → Java bean)

```java
BeanOutputConverter<MovieRecommendation> converter =
    new BeanOutputConverter<>(MovieRecommendation.class);

String format = converter.getFormat(); // Adds JSON schema instruction

String raw = chatClient.prompt()
    .user("Recommend a movie. " + format)
    .call()
    .content();

MovieRecommendation recommendation = converter.convert(raw);
```

---

## Notes

- The model used is `gpt-4o-mini` (cost-effective and fast).
- You can switch to `gpt-4o` or `gpt-3.5-turbo` in `application.properties`.
- Temperature `0.7` gives a balance of creativity and accuracy.
- For production, store the API key in environment variables or a secrets manager.

