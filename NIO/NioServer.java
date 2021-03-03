package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8000);
        serverSocketChannel.socket().bind(address);

        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false);

        // 将serverSocket注册到selector，监听事件：accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            // 等待1秒，如果没有事件发生就继续等待
            if(selector.select(1000) == 0){
                System.out.println("服务器无连接");
            }
            // 有事件发生
            else{
                // 发生事件的selectionKeys
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 遍历集合
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    // 连接请求
                    if(selectionKey.isAcceptable()){
                        // 接收客户端连接
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        // 注册到selector，监听读事件，绑定一个byteBuffer
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }
                    // 读数据请求
                    else if(selectionKey.isReadable()) {
                        // 通过selectionKey获取到channel
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        channel.read(buffer);
                        System.out.println("客户端： " + new String(buffer.array()));
                    }
                    // 把已处理事件的key删除
                    iterator.remove();
                }
            }
        }
    }
}
