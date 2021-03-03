package http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import util.Logger;

public class HttpServer {
    private static final int PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer());

            Logger.info("服务器初始化完成，启动中...");
            ChannelFuture channelFuture = serverBootstrap.bind(PORT);
            channelFuture.addListener(future -> {
                if(channelFuture.isSuccess()){
                    Logger.info("成功绑定端口"+PORT);
                    Logger.info("服务器启动完成");
                }
            });

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Logger.error(e.getMessage());
        } finally {
            Future<?> future = bossGroup.shutdownGracefully();
            future.addListener(future1 -> {
                if(future1.isSuccess()){
                    Logger.info("bossGroup关闭成功");
                }
            });
            Future<?> wFuture = workerGroup.shutdownGracefully();
            wFuture.addListener(future1 -> {
                if(future1.isSuccess()){
                    Logger.info("workerGroup关闭成功");
                }
            });
        }
    }
}
