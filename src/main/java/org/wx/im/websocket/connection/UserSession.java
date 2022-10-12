package org.wx.im.websocket.connection;


import java.util.List;

/**
 * @author WangXin
 * @date 2021-11-12
 */
public interface UserSession {

    /**
     * 添加session
     *
     * @param userId  userId
     * @param sessionId sessionId
     * @return Boolean
     */
    Boolean addSessionId(String userId, String sessionId);

    /**
     * 移除session
     *
     * @param userId    用户id
     * @param sessionId sessionId
     */
    void removeSessionId(String userId, String sessionId);

    /**
     * 获取用户的所有sessionID
     *
     * @param userId    用户id
     * @return sessionID
     */
    List<String> getSessionIds(String userId);

}
