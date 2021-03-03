import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatServer {
    private static final int PORT = 25510;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public GroupChatServer() throws IOException {
        // 初始化selector
        selector = Selector.open();
        // 初始化服务端socket通道
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        // 设置通道为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 绑定通道到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void listen(){
        try{
            while(true){
                // 每100ms监听一次
                if(selector.select(100) > 0){
                    // 获取到发生的事件
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectedKeys.iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        // 建立连接事件
                        if(key.isAcceptable()){
                            SocketChannel acceptedChannel = serverSocketChannel.accept();
                            // 设置连接的channel为非阻塞模式
                            acceptedChannel.configureBlocking(false);
                            // 将新的连接channel注册到selector，设置监听事件为读
                            acceptedChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(acceptedChannel.getRemoteAddress() + " : 建立连接");
                        }
                        // 可读数据事件
                        if(key.isReadable()){
                            // 获取客户端message，并将message转发给其他客户端
                            SocketChannel channel = (SocketChannel)key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            channel.read(buffer);
                            String message = new String(buffer.array()).trim();
                            System.out.println(message);
                            this.dispatchMessage(message, key);
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchMessage(String message, SelectionKey src) throws IOException {
        Set<SelectionKey> keys = selector.keys();
        // 将message转发给所有非src的客户端
        for (SelectionKey key : keys) {
            SelectableChannel channel = key.channel();
            if(channel instanceof SocketChannel && channel != src.channel()){
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                ((SocketChannel) channel).write(buffer);
            }
        }
    }

    private void sendMessage(String message) throws IOException {
        message = "服务端 : " + message;
        Set<SelectionKey> keys = selector.keys();
        for(SelectionKey key : keys){
            SelectableChannel channel = key.channel();
            if(channel instanceof SocketChannel){
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                ((SocketChannel) channel).write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        try {
            GroupChatServer chatServer = new GroupChatServer();
            Thread listenThread = new Thread() {
                @Override
                public void run() {
                    chatServer.listen();
                }
            };

            Thread messagingThread = new Thread() {
                @Override
                public void run() {
                    Scanner scanner = new Scanner(System.in);
                    while(scanner.hasNextLine()){
                        String message = scanner.nextLine();
                        try {
                            chatServer.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            listenThread.start();
            messagingThread.start();
        } catch (IOException e) {
            System.out.println("error");
        }
    }
}
