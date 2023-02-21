package cn.xf.basedemo.common.utils;

import cn.xf.basedemo.common.model.ChatGPTProperties;
import cn.xf.basedemo.config.GlobalConfig;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

import java.util.*;

/**
 * @Description: openAI工具类
 * @ClassName: OpenAIUtils
 * @Author: xiongfeng
 * @Date: 2023/2/20 16:33
 * @Version: 1.0
 */
public class OpenAIUtils {

    public static final String token = "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";
    public static final String model = "text-davinci-003";
    public static final String retries = "20";

    public static String getAnswer(OpenAiService openAiService, ChatGPTProperties chatGPTProperties, String promote) {
        System.out.println("please wait a fell seconds...");
        List<CompletionChoice> choices = new ArrayList();
        int i = 0;
        Random random = new Random();
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model(chatGPTProperties.getModel())
                .prompt(promote)
                .user("DEFAULT USER")
                .temperature(0.9D)
                .topP(1.0D)
                .maxTokens(4000)
                .build();
        while (i < chatGPTProperties.getRetries()) {
            try {
                if (i > 0) {
                    Thread.sleep((long) (500 + random.nextInt(500)));
                }
                System.out.println("第" + (i + 1) + "次请求");
                choices = openAiService.createCompletion(completionRequest).getChoices();
                break;
            } catch (Exception var4) {
                System.out.println(new StringBuilder().append("answer failed ").append(i + 1).append(" times, the error message is: ").append(var4.getMessage()).toString());
                if (i == chatGPTProperties.getRetries() - 1) {
                    System.out.println((i + 1) + "次请求失败");
                    throw new RuntimeException(var4);
                }
                ++i;
            }
        }
        StringBuffer sb = new StringBuffer("");
        for (CompletionChoice choice : choices) {
            String text = choice.getText();
            sb.append(text);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        GlobalConfig globalConfig =new GlobalConfig();
        OpenAiService openAiService = new OpenAiService("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");
        ChatGPTProperties chatGPTProperties = new ChatGPTProperties();
        chatGPTProperties.setModel("text-davinci-003");
        chatGPTProperties.setToken("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");
        chatGPTProperties.setRetries(20);
        String answer = OpenAIUtils.getAnswer(openAiService, chatGPTProperties, "你好");
        Map<String, Object> map = new HashMap<>();
        map.put("answer", answer);
        System.out.println(map);
    }
}
