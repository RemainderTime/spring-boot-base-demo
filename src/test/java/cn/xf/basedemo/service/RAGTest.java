package cn.xf.basedemo.service;


import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;

import java.time.LocalDateTime;
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
    private OllamaChatModel ollamaChatModel;

    @Value("classpath:data/dog.png")
    private org.springframework.core.io.Resource imageResource;

    @Resource(name = "ollamaPgVectorStore")
    private PgVectorStore pgVectorStore;
    @Resource
    private TextSplitter textSplitter;

    /**
     * 上传文件到向量数据库中
     */
    @Test
    public void upload() {
        TikaDocumentReader reader = new TikaDocumentReader("./data/test.md");
        List<Document> documents = reader.get();
        // 2️⃣ 添加 metadata 标签
        for (Document doc : documents) {
            doc.getMetadata().put("knowledge", "ai知识库");
            doc.getMetadata().put("timestamp", LocalDateTime.now().toString());
        }
//        TextSplitter splitter = new TokenTextSplitter(
//                500,   // chunkSize: 每块最多 500 tokens
//                100,   // minChunkSizeChars: 至少 100 个字符才分块
//                50,    // minChunkLengthToEmbed: 小于 50 个字符不进行 embedding
//                1000,  // maxNumChunks: 最多 1000 块
//                true   // keepSeparator: 保留句号、换行符
//        );
        List<Document> splitDocs = textSplitter.split(documents);
        pgVectorStore.accept(splitDocs);
        log.info("上传完成");
    }

    /**
     * 测试图片应答
     */
    @Test
    public void test_call_images() {
        // 构建请求信息
        UserMessage userMessage = UserMessage.builder()
                .media(List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageResource)))
                .text("请帮我描述图片中的内容是什么？")
                .build();
        ChatResponse response = ollamaChatModel.call(new Prompt(
                userMessage,
                OllamaOptions.builder()
                        .model("deepseek-r1:1.5b")
                        .build()));
        log.info("测试结果(images):{}", JSON.toJSONString(response));
    }



    /**
     * 测试 RAG 搜索
     */

    @Test
    public void chat() {
        String message = "人工智能学科始于哪一年";

        String SYSTEM_PROMPT = """
                Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
                If unsure, simply state that you don't know.
                Another thing you need to note is that your reply must be in Chinese!
                DOCUMENTS:
                    {documents}
                """;

        SearchRequest request = SearchRequest.builder()
                .query("小米10年后多少岁")
                .topK(5)
                .filterExpression("knowledge == 'ai知识库'")
                .build();

        List<Document> documents = pgVectorStore.similaritySearch(request);

        String documentsCollectors = documents.stream().map(Document::getText).collect(Collectors.joining());

        Message ragMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage(Map.of("documents", documentsCollectors));

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(message));
        messages.add(ragMessage);

        ChatResponse chatResponse = ollamaChatModel.call(new Prompt(
                messages,
                OllamaOptions.builder()
                        .model("deepseek-r1:1.5b")
                        .build()));
        log.info("测试结果:{}", JSON.toJSONString(chatResponse));
    }


}
