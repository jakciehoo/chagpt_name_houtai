package com.ruoyi.webSocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator
{
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession= (HttpSession) request.getHttpSession();
        //将httpsession对象存储到配置对象中
        //Map<String, Object> getUserProperties();
        //向map集合中存放我们需要的数据，我们可以在其他使用到ServerEndpointConfig对象的类中取出该属性
        //因为都是一个ServerEndpointConfig对象
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
