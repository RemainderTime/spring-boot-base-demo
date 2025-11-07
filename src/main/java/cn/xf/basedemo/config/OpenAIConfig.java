package cn.xf.basedemo.config;


import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * OpenAIConfig
 *
 * @author 海言
 * @date 2025/11/5
 * @time 15:36
 * @Description openai 配置
 */
@Configuration
public class OpenAIConfig {


    @Bean
    public OpenAiApi openAiApi(@Value("${spring.ai.openai.base-url}") String baseUrl, @Value("${spring.ai.openai.api-key}") String apikey) {
        return OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(apikey)
                .build();
    }

    @Bean("openAiSimpleVectorStore")
    public SimpleVectorStore vectorStore(OpenAiApi openAiApi) {
        OpenAiEmbeddingModel embeddingModel = new OpenAiEmbeddingModel(openAiApi);
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    /**
     *
     *
     *
     */
    @Bean("openAiPgVectorStore")
    public PgVectorStore pgVectorStore(OpenAiApi openAiApi, JdbcTemplate jdbcTemplate) {
        OpenAiEmbeddingModel embeddingModel = new OpenAiEmbeddingModel(openAiApi);
        //openai 这里不需要设置向量模型，再调用时动态传入（text-embedding-3-small、text-embedding-3-large）
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName("vector_store_openai")
                .build();
    }
    /**
     *
     -- 删除旧的表（如果存在）
     DROP TABLE IF EXISTS public.vector_store_openai;
     创建新的表，使用UUID作为主键
     create table public.vector_store_openai (
     id UUID primary key default gen_random_uuid(),
     content TEXT not null,
     metadata JSONB,
     embedding VECTOR(1536)
     )
     */

}
