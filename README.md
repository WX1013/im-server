# 单体IM在线聊天

### 1、使用说明

* 首先进行Websocket链接：同时需要做断开重连机制
* 保持每60s一次心跳：每60s发送一次心跳消息，用以维持长连接

参考逻辑流程图

![image-20221011194558841](https://file.iamwx.cn/images/202210111945919.png)

### 2、消息格式说明

```json
{
    "msgType":"BIND", // 消息类型：HEART_BEAT 心跳；BIND 绑定用户；TEXT 发送文本消息
    "from":{ // 消息发送者对象
        "uid":"1", // 消息发送者id
        "name":"用户A", // 消息发送者名称
        "avatar":"http://www.baidu.com/1.png" // 消息发送者头像
    },
    "mid":"7ce4491e-73a7-47bd-9ada-b2a88d5553e4", // 客户端生成的消息id，uuid生成即可
    "to": "2", // 消息接收用户id
    "content": "哈喽，你好，收到消息了吗？" // 消息内容
}
```

### 3、测试

> 浏览器打开两个标签页：http://ws.douqq.com/

#### 1、用户A上线

##### 绑定消息

```json
{
    "msgType":"BIND",
    "from":{
        "uid":"1",
        "name":"用户A",
        "avatar":"http://www.baidu.com/1.png"
    },
    "mid":"7ce4491e-73a7-47bd-9ada-b2a88d5553e4"
}
```

##### 心跳消息

```json
{
    "msgType":"HEART_BEAT",
    "from":{
        "uid":"1",
        "name":"用户A",
        "avatar":"http://www.baidu.com/A.png"
    },
    "mid":"1f0e67f8-727d-48e2-9319-b4ad2c5ec686"
}
```

![image-20221011193806786](https://file.iamwx.cn/images/202210111938121.png)

#### 2、用户B上线

##### 绑定消息

```json
{
    "msgType":"BIND",
    "from":{
        "uid":"2",
        "name":"用户B",
        "avatar":"http://www.baidu.com/B.png"
    },
    "mid":"7ea6acf7-2a4a-4576-bab9-f4fb69d13425"
}
```

##### 心跳消息

```json
{
    "msgType":"HEART_BEAT",
    "from":{
        "uid":"1",
        "name":"用户B",
        "avatar":"http://www.baidu.com/B.png"
    },
    "mid":"7c3f02f6-da27-4619-906e-9afa8b6e3e55"
}
```

![image-20221011193819784](https://file.iamwx.cn/images/202210111938811.png)

#### 3、用户A给用户B发文本消息

```json
{
    "msgType":"TEXT",
    "from":{
        "uid":"1",
        "name":"用户A",
        "avatar":"http://www.baidu.com/A.png"
    },
    "mid":"91ffcc5f-aea9-40cc-bc96-2b4bf31da47a",
    "to": "2",
    "content": "哈喽，你好，收到消息了吗？"
}
```

发送成功后，可看到用户A无消息响应，用户B会有消息响应

用户A

![image-20221011193910593](https://file.iamwx.cn/images/202210111939624.png)

用户B

![image-20221011193903303](https://file.iamwx.cn/images/202210111939332.png)
