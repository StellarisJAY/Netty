package http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        // http编码|解码器
        HttpServerCodec httpServerCodec = new HttpServerCodec();
        pipeline.addLast(httpServerCodec);

        // 添加自定义handler
        pipeline.addLast(new HttpServerHandler());
    }
}
