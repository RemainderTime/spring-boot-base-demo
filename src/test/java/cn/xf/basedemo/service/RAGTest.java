package cn.xf.basedemo.service;


import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RAGTest
 *
 * @author 海言
 * @date 2025/10/27
 * @time 11:11
 * @Description 测试上传知识库文件到 向量数据库中
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RAGTest {

    @Resource
    private OllamaChatClient chatClient;
    @Resource
    private TokenTextSplitter tokenTextSplitter;
    @Resource
    private SimpleVectorStore simpleVectorStore;
    @Resource
    private PgVectorStore pgVectorStore;

    /**
     * 上传文件
     */
    @Test
    public void upload() {
        //解析文件
        TikaDocumentReader reader = new TikaDocumentReader("./data/test01.md");
        List<Document> documents = reader.get();
        //分割文件
        List<Document> documentSplitterList = tokenTextSplitter.apply(documents);
        documents.forEach(doc -> doc.getMetadata().put("knowledge", "知识库名称"));
        //添加元数
        documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge", "知识库名称"));
        //添加向量数据库
        pgVectorStore.accept(documentSplitterList);
        log.info("上传完成");
    }

    /**
     * 测试 RAG 搜索
     */
    @Test
    public void chat() {
        String message = "王大瓜，哪年出生";
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
                .withFilterExpression("knowledge == '知识库名称'");
        List<Document> documents = pgVectorStore.similaritySearch(request);
        String documentCollectors = documents.stream().map(Document::getContent).collect(Collectors.joining());
        Message ragMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage(Map.of("documents", documentCollectors));
        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(message));
        messages.add(ragMessage);
        ChatResponse chatResponse = chatClient.call(
                new Prompt(
                        messages,
                        OllamaOptions.create()
                                .withModel("deepseek-r1:1.5b")
                ));
        log.info("测试结果：{}", JSON.toJSONString(chatResponse));
    }

}
