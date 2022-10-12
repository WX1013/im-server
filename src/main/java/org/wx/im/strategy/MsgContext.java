package org.wx.im.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wx.im.config.exception.BizAssert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WangXin
 * @description 消息管理器
 * @date 2022/3/25 17:36
 */
@Component
@Slf4j
public class MsgContext {

    private final Map<String, MsgStrategy> contextStrategyMap = new ConcurrentHashMap<>();

    public MsgContext(Map<String, MsgStrategy> map) {
        this.contextStrategyMap.putAll(map);
    }

    /**
     * 获取处理器
     *
     * @param storageType 类型
     * @return 处理器
     */
    public MsgStrategy getStrategy(String storageType) {
        MsgStrategy fileStrategy = contextStrategyMap.get(storageType);
        BizAssert.notNull(fileStrategy, "无效的待处理消息类型");
        return fileStrategy;
    }

}
