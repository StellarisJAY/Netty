package http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import util.Logger;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if(httpObject instanceof HttpRequest && httpObject != null){

            Logger.info("请求源地址：" + channelHandlerContext.channel().remoteAddress().toString());
            DefaultHttpRequest request = (DefaultHttpRequest)httpObject;
            Logger.info("请求uri：" +request.uri() + " ; 方法：" + request.method());
            Logger.info("当前pipeline：" + channelHandlerContext.pipeline().hashCode());


            // 服务器发送response
            ByteBuf buffer = Unpooled.copiedBuffer("服务器响应", CharsetUtil.UTF_8);
            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());


            channelHandlerContext.writeAndFlush(httpResponse);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
