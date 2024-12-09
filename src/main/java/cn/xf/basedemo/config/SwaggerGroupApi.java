package cn.xf.basedemo.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * packageName cn.xf.basedemo.config
 * @author remaindertime
 * @className SwaggerGroupApi
 * @date 2024/12/9
 * @description swagger分组配置
 */
@Component
public class SwaggerGroupApi {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Spring boot脚手架 API")
                        .description("开箱即用的Spring boot脚手架 API")
                        .version("v0.0.1")
                        .contact(new Contact().name("remaindertime").url("https://blog.csdn.net/qq_39818325"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Spring boot脚手架 Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("用戶相关分组")
                .pathsToMatch("/user/**")
                .build();
    }
}
