package cn.xf.basedemo.controller.business;

import cn.xf.basedemo.common.model.LoginUser;
import cn.xf.basedemo.common.model.RetObj;
import cn.xf.basedemo.interceptor.SessionContext;
import cn.xf.basedemo.service.UserService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: chat控制器
 * @ClassName: ChatController
 * @Author: xiongfeng
 * @Date: 2023/2/20 16:45
 * @Version: 1.0
 */
@Api(tags = "chat控制器")
@RestController(value = "chat控制器")
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "发送信息", notes = "发送信息")
    @ApiOperationSupport(order = 1)
    @PostMapping("/send")
    public RetObj sendChat(@RequestBody String content){

        return userService.sendChat(content);
    }

}
