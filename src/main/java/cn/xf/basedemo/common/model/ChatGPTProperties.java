package cn.xf.basedemo.common.model;

/**
 * @Description:
 * @ClassName: ChatGPTProperties
 * @Author: xiongfeng
 * @Date: 2023/2/18 16:41
 * @Version: 1.0
 */
public class ChatGPTProperties {

    private String token;
    private String model = "text-davinci-003";
    private Integer retries = 5;

    public ChatGPTProperties() {
    }

    public String getToken() {
        return this.token;
    }

    public String getModel() {
        return this.model;
    }

    public Integer getRetries() {
        return this.retries;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

//    protected boolean canEqual(Object other) {
//        return other instanceof io.github.asleepyfish.config.ChatGPTProperties;
//    }

    public String toString() {
        return "ChatGPTProperties(token=" + this.getToken() + ", model=" + this.getModel() + ", retries=" + this.getRetries() + ")";
    }
}
