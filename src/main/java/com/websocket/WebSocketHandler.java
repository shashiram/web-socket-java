package com.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import java.util.concurrent.TimeUnit;

public class WebSocketHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;

            WebSocketServerHandshakerFactory webSocketServerHandshakerFactory =
                    new WebSocketServerHandshakerFactory("ws://" + request.getUri(), null, true);
            WebSocketServerHandshaker handShaker = webSocketServerHandshakerFactory.newHandshaker(request);
            handShaker.handshake(ctx.channel(), request);

        } else if (msg instanceof WebSocketFrame) {
            System.out.println(((TextWebSocketFrame) msg).text());
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                ctx.writeAndFlush(new TextWebSocketFrame(String.valueOf(Math.random())));
            }
        } else {
            System.out.println(msg);
        }
    }
}
