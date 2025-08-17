package cn.xf.basedemo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * packageName cn.xf.basedemo.config
 * @author remaindertime
 * @className ElasticsearchConfig
 * @date 2024/12/9
 * @description es工具类
 */
@Component
public class EsConfig {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;
    @Value("${elasticsearch.port}")
    private int elasticsearchPort;
    @Value("${elasticsearch.username}")
    private String username;
    @Value("${elasticsearch.password}")
    private String password;

    /**
     -最大连接数 (maxConnTotal)：设置总的最大连接数，取决于业务的并发量。500-2000 之间较为合理。
     -每个节点的最大连接数 (maxConnPerRoute)：控制每个节点的最大连接数，建议 50-100 之间。
     -IO 线程数 (setIoThreadCount)：根据 CPU 核心数设置，通常为 2-4 倍 CPU 核心数。
     -连接超时、套接字超时、获取连接超时：一般设置为 10-30 秒，复杂查询或大数据量操作可适当增加到 20-60 秒。
     -失败监听器 (setFailureListener)：自定义重试和故障处理逻辑，确保高可用性。
     */
    @Bean
    public ElasticsearchClient elasticsearchClient() {

        // 创建凭证提供者
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(username, password)
        );

        // 自定义 RestClientBuilder 配置
        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(elasticsearchHost, elasticsearchPort, "http")
        ).setHttpClientConfigCallback(httpClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider) // 配置认证信息
        );
        // 配置连接超时、套接字超时、获取连接超时
        restClientBuilder.setRequestConfigCallback(builder ->
                builder.setConnectTimeout(20000)
                        .setSocketTimeout(20000)
                        .setConnectionRequestTimeout(20000)
        );
        // 创建 RestClientTransport 和 ElasticsearchClient
        RestClient restClient = restClientBuilder.build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper() // 使用 Jackson 进行 JSON 处理
        );

        return new ElasticsearchClient(transport);
    }

    /**
     window系统本地启动 es8.x 重置密码命令：.\elasticsearch-reset-password -u elastic
     */
}
