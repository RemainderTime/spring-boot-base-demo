package cn.xf.basedemo.config;

import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 * OllamaConfig
 *
 * @author 海言
 * @date 2025/10/24
 * @time 15:35
 * @Description Ollama ai配置类
 */
@Configuration
public class OllamaConfig {


    /**
     * 注入文本分割器
     * @return
     */
    @Bean
    public TokenTextSplitter tokenTextSplitter() {
        return new TokenTextSplitter(500, 100, 50, 1000, true);
    }

    /**
     * 使用 Ollama API 将文本等对象转换成向量
     * 存储在 内存中 或者简单实现
     * 小数据量、临时存储
     * @param ollamaApi
     * @return
     */
    @Bean("ollamaSimpleVectorStore")
    public SimpleVectorStore vectorStore(OllamaApi ollamaApi) {
        OllamaEmbeddingModel embeddingModel = OllamaEmbeddingModel
                .builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(OllamaOptions.builder().model("nomic-embed-text").build())
                .build();
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    /**
     * 使用 Ollama API 生成向量
     * 存储在 PostgreSQL 中持久化
     * 支持向量搜索、分页等数据库操作
     * @param ollamaApi
     * @param jdbcTemplate
     * @return
     */
    @Bean("ollamaPgVectorStore")
    public PgVectorStore pgVectorStore(OllamaApi ollamaApi, JdbcTemplate jdbcTemplate) {
        OllamaEmbeddingModel embeddingModel = OllamaEmbeddingModel
                .builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(OllamaOptions.builder().model("nomic-embed-text").build())
                .build();
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName("vector_store_ollama_deepseek")
                .build();
    }
    //每种模式独立一张向量表
    /**
     create table public.vector_store_ollama_deepseek (
     id UUID primary key default gen_random_uuid(),
     content TEXT not null,
     metadata JSONB,
     embedding VECTOR(768)
     )
     */

}

