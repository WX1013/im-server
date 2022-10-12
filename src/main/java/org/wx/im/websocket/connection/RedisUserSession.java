package org.wx.im.websocket.connection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author WangXin
 * @date 2022/4/22
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class RedisUserSession implements UserSession {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private static final String PREFIX_FORMAT = "chat:%s:%s";

    /**
     * 添加session
     *
     * @param userId userId
     * @param sessionId sessionId
     * @return Boolean
     */
    @Override
    public Boolean addSessionId(String userId, String sessionId) {
        String key = genFormatKey(userId);
        stringRedisTemplate.opsForSet().add(key, sessionId);
        return true;
    }

    /**
     * 移除session
     *
     * @param userId    用户id
     * @param sessionId sessionId
     */
    @Override
    public void removeSessionId(String userId, String sessionId) {
        String key = genFormatKey(userId);
        stringRedisTemplate.opsForSet().remove(key, sessionId);
    }

    /**
     * 获取用户的所有sessionID
     *
     * @param userId 用户id
     * @return sessionID
     */
    @Override
    public List<String> getSessionIds(String userId) {
        String key = genFormatKey(userId);
        Set<String> sessionIds = stringRedisTemplate.opsForSet().members(key);
        return sessionIds == null ? new ArrayList<>() : new ArrayList<>(sessionIds);
    }

    /**
     * 构造缓存Key
     *
     * @param key key
     * @return String
     * @author WangXin
     * @date 2022/10/9 23:17
     **/
    private String genFormatKey(String key) {
        return String.format(PREFIX_FORMAT, "userSession", key);
    }

}
