# websocket
websocket服务器和客户端实现

客户端 TestWebSocketClient.java 基于java-websocket编写
主要maven依赖：
<dependency>
  <groupId>org.java-websocket</groupId>
  <artifactId>Java-WebSocket</artifactId>
  <version>1.3.8</version>
</dependency>

服务端 WebsocketServer.java 在spring boot项目中集成
主要maven依赖：
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>

定时任务 WebsocketPush.java 主要用来验证服务端主动推送消息的情况

服务端实现和客户端实现相对独立可以视情况选择，实时上客户端和服务端还有很多其他实现方式，这里只是挑选了一种相对简单的实现验证形式。
