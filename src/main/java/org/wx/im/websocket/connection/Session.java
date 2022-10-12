package org.wx.im.websocket.connection;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Joker
 * @date 2022/4/22
 */
public class Session {

    private String id;

    private Map<String, Object> attributes = new HashMap<>();

    public Session(String id) {
        this.id = id;
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    public void addAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return id.equals(session.id);
    }
}
