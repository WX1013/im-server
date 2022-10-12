package org.wx.im.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wx.im.config.wrapper.R;
import org.wx.im.model.vo.MsgVO;
import org.wx.im.service.ChatMsgService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author WangXin
 * @description
 * @date 2022/10/11 19:06
 * @classname TestController
 */
@Api(tags = "聊天消息API")
@RestController
@RequestMapping("/msg")
@RequiredArgsConstructor
@Validated
public class MsgController {

    private final ChatMsgService chatMsgService;

    @ApiOperation("获取聊天消息列表，每次最多返回15个")
    @ApiOperationSupport(order = 1)
    @GetMapping("/list")
    public R<List<MsgVO>> listMessage(
            @ApiParam(value = "当前用户id", required = true) @RequestParam @NotEmpty(message = "from can not be null") String from,
            @ApiParam(value = "对方用户id", required = true) @RequestParam @NotEmpty(message = "to can not be null") String to,
            @ApiParam(value = "sort值，第一次传0，后续获取更多消息时，传入当前列表最小sort值", required = true)
            @RequestParam @NotNull(message = "sort can not be null") Integer sort) {
        return R.success(chatMsgService.listMsg(from, to, sort));
    }

}
