package cn.xf.basedemo.controller.business;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * OllamaController
 *
 * @author 海言
 * @date 2025/10/24
 * @time 15:32
 * @Description Ollama本地搭建AI知识库
 */
@RestController
@RequestMapping("/ai/ollama")
public class OllamaController {

    @Resource
    private OllamaChatClient chatClient;

    @Resource
    private PgVectorStore pgVectorStore;

    /**
     * http://localhost:8090/api/v1/ollama/generate?model=deepseek-r1:1.5b&message=1+1
     */
    /**
     * ollama deepseek-r1:1.5b 直接应答
     * @param model
     * @param message
     * @return
     */
    @RequestMapping(value = "generate", method = RequestMethod.GET)
    public ChatResponse generate(@RequestParam("model") String model, @RequestParam("message") String message) {
        return chatClient.call(new Prompt(message, OllamaOptions.create().withModel(model)));
    }

    /**
     * http://localhost:8090/api/v1/ollama/generate_stream?model=deepseek-r1:1.5b&message=hi
     */
    /**
     *  ollama deepseek-r1:1.5b 流式应答
     * @param model
     * @param message
     * @return
     */
    @RequestMapping(value = "generate_stream", method = RequestMethod.GET)
    public Flux<ChatResponse> generateStream(@RequestParam("model") String model, @RequestParam("message") String message) {
        return chatClient.stream(new Prompt(message, OllamaOptions.create().withModel(model)));
    }

    /**
     * http://localhost:8090/api/v1/ollama/generate_stream_rag?model=deepseek-r1:1.5b&ragTag=ragTag&message=hi
     */
    /**
     *  ollama deepseek-r1:1.5b 带rag的流式应答
     * @param model
     * @param ragTag
     * @param message
     * @return
     */
    @RequestMapping(value = "generate_stream_rag", method = RequestMethod.GET)
    public Flux<ChatResponse> generateStreamRag(@RequestParam("model") String model, @RequestParam("ragTag") String ragTag, @RequestParam("message") String message) {
        String SYSTEM_PROMPT = """
                Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
                If unsure, simply state that you don't know.
                Another thing you need to note is that your reply must be in Chinese!
                DOCUMENTS:
                    {documents}
                """;

        // 指定文档搜索
        SearchRequest request = SearchRequest.query(message)
                .withTopK(5)
                .withFilterExpression("knowledge == '" + ragTag + "'");

        List<Document> documents = pgVectorStore.similaritySearch(request);
        String documentCollectors = documents.stream().map(Document::getContent).collect(Collectors.joining());
        Message ragMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage(Map.of("documents", documentCollectors));

        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(message));
        messages.add(ragMessage);

        return chatClient.stream(new Prompt(
                messages,
                OllamaOptions.create()
                        .withModel(model)
        ));
    }

}
