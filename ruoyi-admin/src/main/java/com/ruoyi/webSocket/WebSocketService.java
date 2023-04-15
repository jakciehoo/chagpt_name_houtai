package com.ruoyi.webSocket;

import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.service.IChatGtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created with IntelliJ IDEA.
 * @ Description:
 * @ ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@Component
@Service
@ServerEndpoint(value = "/websocket/chat/{userId}")
public class WebSocketService {

    static Logger log = LoggerFactory.getLogger(WebSocketService.class);
    //当前在线连接数
    private static int onlineCount = 0;
    //存放每个客户端对应的MyWebSocket对象
    private static final CopyOnWriteArraySet<WebSocketService> webSocketSet = new CopyOnWriteArraySet<WebSocketService>();

    private Session session;

    //接收userId
    private String userId = "";

    //自动注入service
    private static IChatGtpService iChatGtpService;
    @Autowired
    public void setIChatGtpService(IChatGtpService iChatGtpService){
        WebSocketService.iChatGtpService=iChatGtpService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        this.userId = userId;
        addOnlineCount();           //在线数加1
        try {
            sendMessage("isOnlineSucc");
            log.info("有新窗口开始监听:" + userId + ",当前在线人数为:" + getOnlineCount());
        } catch (IOException e) {
            log.error("websocket IO Exception");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1

        log.info("释放的userId为："+ userId);

        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());

    }

    /**
     * 收到客户端消息后调用的方法
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //群发消息
        for (WebSocketService item : webSocketSet) {
//            try {
////                JSONObject jsonObject = new JSONObject();
////                jsonObject.put("fromUser",userId);
////                jsonObject.put("msg",message);
////                item.sendMessage(jsonObject.toJSONString());
////
////                //回复用户
////                String apikey = "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";
////                //请求URL
////                String url1 =  "https://chatapi.broue.cn/v1/chat/completions";
////                URL url = new URL(url1); // 接口地址
////                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
////                urlConnection.setRequestMethod("POST");
////                urlConnection.setDoOutput(true);
////                urlConnection.setDoInput(true);
////                urlConnection.setUseCaches(false);
////                urlConnection.setRequestProperty("Connection", "Keep-Alive");
////                urlConnection.setRequestProperty("Charset", "UTF-8");
////                urlConnection.setRequestProperty("Authorization", "Bearer " + apikey);
////                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
////
////                String input = message;
////                Gpt35TurboVO gpt35TurboVO = new Gpt35TurboVO();
////                gpt35TurboVO.setRole("user");
////                gpt35TurboVO.setContent(input);
////                List<Gpt35TurboVO> objects = new ArrayList<>();
////                objects.add(gpt35TurboVO);
////                Map<Object, Object> objectObjectHashMap = new HashMap<>();
////                objectObjectHashMap.put("model", "gpt-3.5-turbo");
////                objectObjectHashMap.put("messages", objects);
////                objectObjectHashMap.put("stream", true);
////                objectObjectHashMap.put("temperature", 0);
////                objectObjectHashMap.put("frequency_penalty", 0);
////                objectObjectHashMap.put("presence_penalty", 0.6);
////                String postData = JSONUtil.toJsonStr(objectObjectHashMap);
////                byte[] dataBytes = postData.getBytes();
////                urlConnection.setRequestProperty("Content-Length", String.valueOf(dataBytes.length));
////                OutputStream os = urlConnection.getOutputStream();
////                os.write(dataBytes);
////                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
////
//                String userId = SecurityUtils.getUserId()+"";
//                System.out.println(userId);
//                if (StrUtil.isBlank(message)){
//                    throw new RuntimeException("内容不可为空");
//                }
//                OutputStream os = null;
//                InputStream is = null;
//                 //进行封装查询内容
//                iChatGtpService.sendRequestBefore(os,message);
//                String line =null;
//                BufferedReader reader = null;
//                reader = new BufferedReader(new InputStreamReader(is));
//                while((line = reader.readLine()) != null) {
//                    if (StrUtil.isNotBlank(line) && !StrUtil.contains(line,"[DONE]")){
//                        line = CollectionUtil.removeEmpty(StrUtil.split(line, "data: ")).get(0);
//                    }
//                    item.sendMessage(line);
//                    TimeUnit.MILLISECONDS.sleep(100);
//                }
//                os.flush();
//                os.close();
//                is.close();
//                reader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }

    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        log.info("推送消息到窗口" + userId + "，推送内容:" + message);

        for (WebSocketService item : webSocketSet) {
            try {
                //为null则全部推送
                if (userId == null) {
                    item.sendMessage(message);
                } else if (item.userId.equals(userId)) {
                    item.sendMessage(message);
                }
            } catch (Exception e) {
                continue;
            }
        }
    }


    /**
     * 群发自定义消息
     */
    public static void sendWord(String message) {
        for (WebSocketService item : webSocketSet) {
            try {
                //推送全部人消息
                item.sendMessage(message);
            } catch (Exception e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }

    public static CopyOnWriteArraySet<WebSocketService> getWebSocketSet() {
        return webSocketSet;
    }
}
